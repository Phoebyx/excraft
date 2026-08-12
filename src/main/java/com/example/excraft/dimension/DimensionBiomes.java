package com.example.excraft.dimension;

import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class DimensionBiomes {

    public static BiomeSource buildRandomBiomes(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Holder<MultiNoiseBiomeSourceParameterList> biomeParameter = registryAccess.holderOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
        BiomeSource biomeSource = MultiNoiseBiomeSource.createFromPreset(biomeParameter);
        return biomeSource;
        }
}

