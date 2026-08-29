package com.example.excraft;

import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.blocks.ExcraftPortalBeaconBlockEntity;
import com.example.excraft.blocks.ExcraftPortalBeaconEntityRenderer;
import com.example.excraft.blocks.ExcraftPortalTint;
import com.example.excraft.data.ExcraftCreativeModeTab;
import com.example.excraft.data.ExcraftDataRegisters;
import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.*;
//import com.example.excraft.infiniverse.internal.UpdateDimensionsPacket;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierManager;
import com.example.excraft.data.WorldModifierRegister;
import com.example.excraft.dimension.worldmodifiers.WorldModifierManagerSavedData;
import com.example.excraft.inventory.DurabilityChanges;
import com.example.excraft.portal.PlaceOriginPortal;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.commoble.infiniverse.internal.DimensionManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
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

import java.util.Arrays;
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
        modEventBus.addListener(this::registerTint);
        modEventBus.addListener(this::registerRegistries);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (excraft) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(ExcraftCommands::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(DisabledDimensionBlocks::preventDisabledBlocks);
        NeoForge.EVENT_BUS.addListener(DurabilityChanges::recordWhoLeft);
        NeoForge.EVENT_BUS.addListener(DurabilityChanges::damageByPortalReturn);
        WorldModifierRegister.registerModifierEventListeners();

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::addCreative);
       // modEventBus.addListener(this::onRegisterPayloadHandlers);
    }
/*
    void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event)
    {
        event.registrar(MODID)
                .optional()
                .playToClient(UpdateDimensionsPacket.TYPE, UpdateDimensionsPacket.STREAM_CODEC, UpdateDimensionsPacket::handle);
    }
*/
    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
    }

    @SubscribeEvent
    private void onServerStarted(ServerStartedEvent event) {
        WorldModifierManagerSavedData.compute(event.getServer().overworld().getDataStorage());
        ExcraftDimensionManager.setCurrentManagerFromSave(event);
        MinecraftServer server = event.getServer();
        PlaceOriginPortal.placeBarrenRealmPortal(server);
        if (!ExcraftDimensionManager.doesDimensionExist(server)) {
            createDimension(event.getServer());
        }
    }
     // on the mod event bus
    public void registerRegistries(NewRegistryEvent event) {
        ExcraftDataRegisters.registerRegistries(event);
    }

    private void registerTint(RegisterColorHandlersEvent.Block event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ExcraftPortalTint.registerBlockColorHandlers(event);
        }
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        ExcraftCreativeModeTab.addCreative(event);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppingEvent event) {
    }

    @SubscribeEvent
    public void onPreServerTick(ServerTickEvent.Pre event) {
        if (ExcraftDimensionManager.doesDimensionExist(event.getServer())) {
            int getColorIndex = ExcraftTimer.intPortalBlockColor(event.getServer().overworld());
            if (ExcraftTimer.isLastColorIndexSelectedSameAsLast(getColorIndex,event)) {
                ExcraftTimer.updateLastColorIndexSelected(getColorIndex);
                ExcraftPortalTint.updatePortalColorOnTickSchedule(event);
            }
            if (ExcraftTimer.getCurrentTimer(event.getServer()) <= 1) {
                Excraft.LOGGER.info("Timer is up! Resetting Dimension");
                createDimension(event.getServer());
                ExcraftPortalTint.updatePortalColorOnTickSchedule(event);
            }
        } else {
            ExcraftDimensionManager.createDimension(event.getServer());
        }
    }

    @SubscribeEvent
    public void worldModifierScheduler(ServerTickEvent.Pre event) {
        WorldModifierManager manager = ExcraftDimensionManager.getCurrentManager();
        if (manager != null && ExcraftDimensionManager.doesDimensionExist(event.getServer())) {
            manager.worldModifierScheduler();
        }
    }

    public static void createDimension(MinecraftServer server) {
        if (ExcraftDimensionManager.doesDimensionExist(server)) {
            Excraft.LOGGER.info("Deleting");
            deleteDimension(server);
        }
        ExcraftDimensionManager.createDimension(server);
    }

    public static void deleteDimension(MinecraftServer server) {
        try {
            ExcraftDimensionManager.deleteDimension(server);
            ExcraftDimensionManager.clearUnusedDimension(server);
        } catch (Exception ignored) {}
         Excraft.LOGGER.info("Deleted Old Dimension");
    }
}
