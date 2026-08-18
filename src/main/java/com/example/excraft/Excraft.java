package com.example.excraft;

import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.data.ExcraftCreativeModeTab;
import com.example.excraft.data.ExcraftDataRegisters;
import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.DimensionManager;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.DisabledDimensionBlocks;
import com.example.excraft.portal.PlaceOriginPortal;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.Set;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Excraft.MODID)
public class Excraft {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "excraft";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Excraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ExcraftDataRegisters.registerRegisters(modEventBus);
        modEventBus.addListener(ExcraftDataRegisters::onGatherData);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (excraft) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(DisabledDimensionBlocks::preventDisabledBlocks);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
    }
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
         MinecraftServer server = event.getServer();
         if (server.overworld().getBlockState(new BlockPos(0,-3,0)).getBlock() != ExcraftBlocks.UNBREAKABLE_SANDSTONE.get()) {
             PlaceOriginPortal.placeBarrenRealmPortal(server);
         }
         if (!DimensionManager.doesDimensionExist(server)) {
             createDimension(event.getServer());
         }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        ExcraftCreativeModeTab.addCreative(event);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public void onServerStopped(ServerStoppingEvent event) {
    }

    @SubscribeEvent
    public void onPreServerTick(ServerTickEvent.Pre event) {
        if (DimensionManager.doesDimensionExist(event.getServer())) {
            if (ExcraftTimer.getCurrentTimer(event.getServer()) <= 1) {
                Excraft.LOGGER.info("Timer is up! Resetting Dimension");
                createDimension(event.getServer());
            }
        } else {
            DimensionManager.createDimension(event.getServer());
        }
    }

    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(Commands.literal("excraft")
                .then(Commands.literal("createDimension")
                        .executes(context -> {
                            MinecraftServer server = context.getSource().getServer();
                            createDimension(server);
                            return 1;
                        }))
                .then(Commands.literal("deleteDimension")
                        .executes(context -> {
                            MinecraftServer server = context.getSource().getServer();
                            deleteDimension(server);
                            return 1;
                        }))
                .then(Commands.literal("seed")
                        .then(Commands.argument("seed", IntegerArgumentType.integer())
                                .executes(context -> {
                                    DimensionRandomizer.manualSalt(IntegerArgumentType.getInteger(context,"seed"));
                                    return 1;
                                }))
                )
                .then(Commands.literal("readSeed")
                        .executes(context -> {
                                    int salt = DimensionRandomizer.getSalt();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(String.valueOf(salt)),
                                            true
                                );
                                    return 1;
                        })
                )
                .then(Commands.literal("tp")
                        .executes(this::tpPlayerToExcraft)
                )
                .then(Commands.literal("timer")
                        .executes(context ->  {
                            MinecraftServer server = context.getSource().getServer();
                            context.getSource().sendSuccess(
                                    () -> Component.literal(ExcraftTimer.getCurrentTimerInHumanReadableForm(server)),
                                    true
                            );
                                    return 1;
                        })
                )
        );

    }

    private int tpPlayerToExcraft(CommandContext<CommandSourceStack> context) {
         MinecraftServer server = context.getSource().getServer();
         Player target = context.getSource().getPlayer();
         if (target.level() != server.getLevel(DimensionManager.EXCRAFT_LEVEL)) {
             target.teleportTo(server.getLevel(DimensionManager.EXCRAFT_LEVEL), target.getX(),target.getY(),target.getZ(),Set.of(),0,0);
         } else {
             target.teleportTo(server.overworld(), target.getX(),target.getY(),target.getZ(),Set.of(),0,0);
         }
         return 1;
    }

    public void createDimension(MinecraftServer server) {
        if (DimensionManager.doesDimensionExist(server)) {
            Excraft.LOGGER.info("Deleting");
            deleteDimension(server);
        }
        DimensionManager.createDimension(server);
    }

    public void deleteDimension(MinecraftServer server) {
        try {
            DimensionManager.deleteDimension(server);
            DimensionManager.clearUnusedDimension(server);
        } catch (Exception ignored) {}
         Excraft.LOGGER.info("Deleted Old Dimension");
    }
}
