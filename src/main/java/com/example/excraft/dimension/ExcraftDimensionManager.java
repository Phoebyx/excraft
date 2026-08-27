package com.example.excraft.dimension;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftPortalBlock;
//import com.example.excraft.infiniverse.api.InfiniverseAPI;
//import com.example.excraft.infiniverse.internal.DimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifierManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifierManagerSavedData;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;


public class ExcraftDimensionManager {
    public static final ResourceKey<Level> EXCRAFT_LEVEL = ResourceKey.create(Registries.DIMENSION,ResourceLocation.fromNamespaceAndPath(Excraft.MODID, "excraft"));
    private static WorldModifierManager currentManager;

    public static void createDimension(MinecraftServer server) {
        Excraft.LOGGER.info("Starting Dimension Creation");
        DimensionRandomizer.randomizeSalt();
        int tempInt = setModifierSlots();
        currentManager = new WorldModifierManager(server,tempInt);
        WorldModifierManagerSavedData worldModifierManagerSavedData = WorldModifierManagerSavedData.compute(server.overworld().getDataStorage());
        worldModifierManagerSavedData.modifyData();
        DimensionCreator newDimension = new DimensionCreator();
        DimensionType dimensionType = newDimension.makeDimensionType();
        InfiniverseAPI.get().getOrCreateLevel(server, EXCRAFT_LEVEL, () -> newDimension.makeStem(dimensionType,server));
        ExcraftPortalBlock.createPortalAndReturnLocation(server.getLevel(EXCRAFT_LEVEL));
        server.saveEverything(false,true,true);
    }
    private static int setModifierSlots() {
        if (Config.ROLLMODIFIERSRANGEMIN.get() == Config.ROLLMODIFIERSRANGEMAX.get()) {
            return Config.ROLLMODIFIERSRANGEMIN.get();
        } else {
           return DimensionRandomizer.generateRandomFromSalt().nextInt(Config.ROLLMODIFIERSRANGEMIN.get(),Config.ROLLMODIFIERSRANGEMAX.get());
        }
    }

    public static WorldModifierManager getCurrentManager() {
        return currentManager;
    }

    public static void setCurrentManagerFromSave(ServerStartedEvent event) {
        currentManager = WorldModifierManagerSavedData.compute(event.getServer().overworld().getDataStorage()).returnSavedManager(event.getServer());
        Excraft.LOGGER.info("Gotten Saved Manager with " + currentManager.getCurrentModifiers() + " and salt " + currentManager.getSalt());
    }

    public static void deleteDimension(MinecraftServer server) {
        currentManager.disableModifierEffects();
        InfiniverseAPI.get().markDimensionForUnregistration(server, EXCRAFT_LEVEL);
    }

    public static void clearUnusedDimension(MinecraftServer server) {
            Path dimensionPath = server.getWorldPath(LevelResource.ROOT).resolve("dimensions/excraft");
            try {
                Files.walk(dimensionPath).sorted(Comparator.reverseOrder()).forEach(path -> {
                            try {
                         Files.delete(path);
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static boolean doesDimensionExist(MinecraftServer server) {
        try {
            return server.getLevel(EXCRAFT_LEVEL).getGameTime() >= 0;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
