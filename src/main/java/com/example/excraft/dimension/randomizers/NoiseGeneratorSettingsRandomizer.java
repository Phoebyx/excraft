package com.example.excraft.dimension.randomizers;

import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class NoiseGeneratorSettingsRandomizer {

    public static Holder<NoiseGeneratorSettings> noiseTransformer(Holder<NoiseGeneratorSettings> implementNoise, MinecraftServer server) {
        NoiseGeneratorSettings noiseToModify = implementNoise.value();
        NoiseRouterRandomizer routerRandomizer = new NoiseRouterRandomizer(noiseToModify,server);
        NoiseGeneratorSettings modifiedNoise = new NoiseGeneratorSettings(
                noiseToModify.noiseSettings(),
                noiseToModify.defaultBlock(),
                noiseToModify.defaultFluid(),
                routerRandomizer.makeNoiseRouter(),
                noiseToModify.surfaceRule(),
                noiseToModify.spawnTarget(),
                noiseToModify.seaLevel(),
                false,
                noiseToModify.aquifersEnabled(),
                noiseToModify.oreVeinsEnabled(),
                noiseToModify.useLegacyRandomSource()
        );
        return Holder.direct(modifiedNoise);
    }
}
