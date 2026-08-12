package com.example.excraft.dimension;

import com.example.excraft.Excraft;
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
    public static boolean doesDimensionExist = false;

    public static void createDimension(MinecraftServer server) {
        DimensionCreator newDimension = new DimensionCreator();
        DimensionType dimensionType = newDimension.makeDimensionType();
        ServerLevel oldLevel = server.overworld();
        Holder<DimensionType> typeHolder = oldLevel.dimensionTypeRegistration();
        InfiniverseAPI.get().getOrCreateLevel(server, EXCRAFT_LEVEL, () -> newDimension.makeStem(typeHolder.value(),server));
        doesDimensionExist = true;
    }
    public static void deleteDimension(MinecraftServer server) {
        InfiniverseAPI.get().markDimensionForUnregistration(server, EXCRAFT_LEVEL);
        doesDimensionExist = false;
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
    public static boolean doesDimensionExist() {
        return doesDimensionExist;
    }
}
