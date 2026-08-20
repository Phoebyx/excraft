package com.example.excraft.dimension;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.security.Provider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ModifiedNoiseBasedChunkGenerator extends NoiseBasedChunkGenerator {
    private MinecraftServer serverLevel;
    private RandomState overridenRandom;
    public ModifiedNoiseBasedChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, MinecraftServer serverLevel) {
        super(biomeSource, settings);
        this.serverLevel = serverLevel;
        this.overridenRandom = randomStateGenerator();
    }

    public RandomState randomStateGenerator() {
        HolderGetter<NormalNoise.NoiseParameters> holderGetter = serverLevel.registryAccess().asGetterLookup().lookupOrThrow(Registries.NOISE);
        return RandomState.create(generatorSettings().value(),holderGetter,DimensionRandomizer.generateRandomFromSalt().nextLong());
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunk) {
        return super.createBiomes(overridenRandom,blender,structureManager,chunk);
    }
    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return super.getBaseHeight(x,z,type,level,overridenRandom);
    }
    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
        return super.getBaseColumn(x,z,height,overridenRandom);
    }
    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
        super.addDebugScreenInfo(info,overridenRandom,pos);
    }

    @Override
    protected OptionalInt iterateNoiseColumn(LevelHeightAccessor level, RandomState random, int x, int z, @Nullable MutableObject<NoiseColumn> column, @Nullable Predicate<BlockState> stoppingState) {
        return super.iterateNoiseColumn(level,overridenRandom,x,z,column,stoppingState);
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
        super.buildSurface(level,structureManager,overridenRandom,chunk);
    }
    @Override
    @VisibleForTesting
    public void buildSurface(ChunkAccess chunk, WorldGenerationContext context, RandomState random, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomes, Blender blender) {
       super.buildSurface(chunk,context,overridenRandom,structureManager,biomeManager,biomes,blender);
    }
    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
        super.applyCarvers(level,DimensionRandomizer.generateRandomFromSalt().nextLong(),overridenRandom,biomeManager,structureManager,chunk,step);
    }
    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return super.fillFromNoise(blender,overridenRandom,structureManager,chunk);
    }
}
