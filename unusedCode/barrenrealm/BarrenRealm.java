package com.example.excraft.barrenrealm;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.List;
import java.util.Optional;

public class BarrenRealm {

    private static boolean lakes = false;
    private static boolean decoration = true;

    public static LevelStem barrenRealmLevelStem(BootstrapContext<WorldPreset> context) {
        return new LevelStem(overworld(context),generator(context));
    }
    private static Holder<DimensionType> overworld(BootstrapContext<WorldPreset> context) {
        return context.lookup(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD);
    }
    public static FlatLevelSource generator(BootstrapContext<WorldPreset> context) {
        List<Holder<PlacedFeature>> lakes = List.of();
        FlatLevelGeneratorSettings flatSettings = new FlatLevelGeneratorSettings(
                Optional.empty(),
                getBiome(context),
                lakes
        );
        flatSettings = flatSettings.withBiomeAndLayers(
                layerList(),
                Optional.empty(),
                getBiome(context)
        );
        return new FlatLevelSource(flatSettings);
    }

    private static List<FlatLayerInfo> layerList() {
        return List.of(
            new FlatLayerInfo(2, Blocks.BEDROCK),
            new FlatLayerInfo(20,Blocks.SANDSTONE),
            new FlatLayerInfo(5, Blocks.SAND)
        );
    }
    private static Holder<Biome> getBiome(BootstrapContext<WorldPreset> context) {
        return context.lookup(Registries.BIOME).getOrThrow(Biomes.DESERT);
    }
}
