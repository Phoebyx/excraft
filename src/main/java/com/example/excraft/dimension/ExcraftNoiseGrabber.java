package com.example.excraft.dimension;

import com.example.excraft.Excraft;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.apache.logging.log4j.core.jmx.Server;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ExcraftNoiseGrabber {

    public static NoiseBasedChunkGenerator buildRandomNoise(BiomeSource source,MinecraftServer server) {
        ResourceKey<NoiseGeneratorSettings> currentNoise = getNoise(server);
        Excraft.LOGGER.info("Current noise" + currentNoise.toString());
        RegistryAccess registryAccess = server.registryAccess();
        Holder<NoiseGeneratorSettings> implementNoise = registryAccess.registryOrThrow(Registries.NOISE_SETTINGS).getHolderOrThrow(currentNoise);
        NoiseBasedChunkGenerator noise = new NoiseBasedChunkGenerator(source,implementNoise);
        Excraft.LOGGER.info("Current Chunk Noise" + noise.toString());
        return noise;
    }

    public static ResourceKey<NoiseGeneratorSettings> getNoise(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Registry<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registries.NOISE_SETTINGS);
        Optional<Holder.Reference<NoiseGeneratorSettings>> randomEntry = noiseSettings.getRandom(DimensionRandomizer.generateRandomFromSalt());
        return randomEntry.orElseThrow().getKey();
    }
}
//server.overworld().getRandom()