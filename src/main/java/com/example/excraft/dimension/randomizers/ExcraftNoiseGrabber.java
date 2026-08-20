package com.example.excraft.dimension.randomizers;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ModifiedNoiseBasedChunkGenerator;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;

public class ExcraftNoiseGrabber {

    public static ModifiedNoiseBasedChunkGenerator buildRandomNoise(BiomeSource source, MinecraftServer server) {
        ResourceKey<NoiseGeneratorSettings> currentNoise = getNoise(server);
        Excraft.LOGGER.info("Current noise" + currentNoise.toString());
        RegistryAccess registryAccess = server.registryAccess();
        Holder<NoiseGeneratorSettings> implementNoise = registryAccess.registryOrThrow(Registries.NOISE_SETTINGS).getHolderOrThrow(currentNoise);
        Holder<NoiseGeneratorSettings> finalNoise = NoiseGeneratorSettingsRandomizer.noiseTransformer(implementNoise,server);
        ModifiedNoiseBasedChunkGenerator noise = new ModifiedNoiseBasedChunkGenerator(source,finalNoise,server);
        Excraft.LOGGER.info("Current Chunk Noise" + noise.toString());
        return noise;
    }
    public static ResourceKey<NoiseGeneratorSettings> getNoise(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Registry<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registries.NOISE_SETTINGS);
        List<ResourceKey<NoiseGeneratorSettings>> noiseList = getNoiseList(noiseSettings);
        int randInt = DimensionRandomizer.generateRandomFromSalt().nextInt() % noiseList.size();
        return noiseList.get(Math.abs(randInt));
    }

    private static List<ResourceKey<NoiseGeneratorSettings>> getNoiseList(Registry<NoiseGeneratorSettings> noiseSettings) {
        List<ResourceKey<NoiseGeneratorSettings>> list = new ArrayList<>();
        for (Map.Entry<ResourceKey<NoiseGeneratorSettings>, NoiseGeneratorSettings> entry : noiseSettings.entrySet()) {
            if (!isNoiseInBlacklist(entry)) {
                    list.addLast(entry.getKey());
            }
        }
        return list;
    }
    private static boolean isNoiseInBlacklist(Map.Entry<ResourceKey<NoiseGeneratorSettings>,NoiseGeneratorSettings> entry) {
        List<ResourceLocation> blacklist = returnNamespaceAndPath();
        for (ResourceLocation blacklistEntry : blacklist) {
            if (entry.getKey().location().toString().equals(blacklistEntry.toString())) {
               return true;
            }
        }
        return false;
    }

    private static List<ResourceLocation> returnNamespaceAndPath() {
        List<ResourceLocation> validNoises = new ArrayList<>();
        for (String toBeValidated : Config.NOISE_BLACKLIST.get()) {
            String[] separated = toBeValidated.split(":");
            validNoises.addLast(ResourceLocation.fromNamespaceAndPath(separated[0],separated[1]));
        }
        validNoises.addLast(ResourceLocation.fromNamespaceAndPath("excraft","null"));
        return validNoises;
    }
}
//server.overworld().getRandom()