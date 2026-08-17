package com.example.excraft.dimension;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import java.util.OptionalLong;

public class DimensionCreator {
    private static final double coordinateScale = 1;
    private static final boolean bedWorks = true;
    private static final boolean respawnAnchorWorks = true;
    private static final int minY = -64;
    private static final int height = 384 ;
    private static final int logicalHeight = 256;
    //Maybe change this to be randomized too
    private static final TagKey<Block> infiniburnType = BlockTags.INFINIBURN_OVERWORLD;
    private static final ResourceLocation effect = ResourceLocation.fromNamespaceAndPath("minecraft","overworld");
    private static final DimensionType.MonsterSettings monsterSettings = new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0);

    public LevelStem makeStem(DimensionType dimensionType, MinecraftServer server) {
        BiomeSource source = DimensionBiomes.buildRandomBiomes(server);
        NoiseBasedChunkGenerator excraftRandomNoise = ExcraftNoiseGrabber.buildRandomNoise(source,server);
        Holder<DimensionType> hdimensionType = server.registryAccess().holderOrThrow(BuiltinDimensionTypes.OVERWORLD);
        return new LevelStem(hdimensionType, excraftRandomNoise);
    }

    public DimensionType makeDimensionType() {
        DimensionRandomizer dimension = new DimensionRandomizer();
        DimensionType dimensionType = new DimensionType(
                OptionalLong.empty(),
                dimension.hasSkyLight(),
                dimension.hasCeiling(),
                dimension.isUltraWarm(),
                dimension.isNatural(),
                coordinateScale,
                bedWorks,
                respawnAnchorWorks,
                minY,
                height,
                logicalHeight,
                infiniburnType,
                effect,
                dimension.getAmbientLight(),
                monsterSettings
        );
        return dimensionType;
    }
}

