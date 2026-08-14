package com.example.excraft;

import com.example.excraft.data.ExcraftCreativeModeTab;
import com.example.excraft.data.ExcraftDataRegisters;
import com.example.excraft.dimension.DimensionManager;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.portal.PlaceOriginPortal;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
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

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
    }
    @SubscribeEvent
    public void portalPlacer(ServerStartedEvent event) {
         MinecraftServer server = event.getServer();
         PlaceOriginPortal.placeBarrenRealmPortal(server,event);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        ExcraftCreativeModeTab.addCreative(event);
    }

    // Add the example block item to the building blocks tab

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(Commands.literal("excraft")
                .then(Commands.literal("createdimension")
                        .executes(this::createDimension))
                .then(Commands.literal("deletedimension")
                        .executes(this::deleteDimension))
                .then(Commands.literal("seed")
                        .then(Commands.argument("seed", IntegerArgumentType.integer())
                                .executes(context -> {
                                    DimensionRandomizer.manualSalt(IntegerArgumentType.getInteger(context,"seed"));
                                    return 1;
                                }))
                )
                .then(Commands.literal("readseed")
                        .executes(context -> {
                                    int salt = DimensionRandomizer.getSalt();
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(String.valueOf(salt)),
                                            true
                                );
                                    return 1;
                        })
                )
                .then(Commands.literal("excrafttp")
                        .executes(this::tpPlayerToExcraft)
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

    public int createDimension(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
         try {
             MinecraftServer server = context.getSource().getServer();
             if (DimensionManager.doesDimensionExist()) {
                 DimensionManager.deleteDimension(server);
                 wait(2000);
                 DimensionManager.clearUnusedDimension(server);
                 wait(2000);
             }
             DimensionManager.createDimension(server);
         } catch (Exception e) {
             throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
         }
         return 1;
    }

    public int deleteDimension(CommandContext<CommandSourceStack> context) {
         MinecraftServer server = context.getSource().getServer();
         DimensionManager.deleteDimension(server);
         DimensionManager.clearUnusedDimension(server);
         return 1;
    }
}
