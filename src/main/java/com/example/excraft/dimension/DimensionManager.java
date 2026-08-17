package com.example.excraft.dimension;

import com.example.excraft.Excraft;
import com.example.excraft.data.ExcraftTimer;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Supplier;


public class DimensionManager {
    public static final ResourceKey<Level> EXCRAFT_LEVEL = ResourceKey.create(Registries.DIMENSION,ResourceLocation.fromNamespaceAndPath(Excraft.MODID, "excraft"));

    public static void createDimension(MinecraftServer server) {
        DimensionRandomizer.randomizeSalt();
        DimensionCreator newDimension = new DimensionCreator();
        DimensionType dimensionType = newDimension.makeDimensionType();
        ServerLevel oldLevel = server.overworld();
        Holder<DimensionType> typeHolder = oldLevel.dimensionTypeRegistration();
        InfiniverseAPI.get().getOrCreateLevel(server, EXCRAFT_LEVEL, () -> newDimension.makeStem(typeHolder.value(),server));
    }

    public static void deleteDimension(MinecraftServer server) {
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
            Excraft.LOGGER.info("Getting is handlingTick" + server.getLevel(EXCRAFT_LEVEL));
            return server.getLevel(EXCRAFT_LEVEL).getGameTime() >= 0;
        } catch (NullPointerException e) {
            Excraft.LOGGER.info("Getting is handlingTick, but it was Null");
            return false;
        }
    }
    public static boolean areFilesDeleted(MinecraftServer server) {
        Path dimensionPath = server.getWorldPath(LevelResource.ROOT).resolve("dimensions/excraft");
        return dimensionPath.toFile().exists();
    }
}
