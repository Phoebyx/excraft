package com.example.excraft.dimension.randomizers;

import net.minecraft.core.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.*;

public class DimensionBiomes {

    public static BiomeSource buildRandomBiomes(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Holder<MultiNoiseBiomeSourceParameterList> biomeParameter = registryAccess.holderOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
        BiomeSource biomeSource = MultiNoiseBiomeSource.createFromPreset(biomeParameter);
        return biomeSource;
        }
}

