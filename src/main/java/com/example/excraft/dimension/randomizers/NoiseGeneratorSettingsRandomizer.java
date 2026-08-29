package com.example.excraft.dimension.randomizers;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.NoiseGeneratorSettingsWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class NoiseGeneratorSettingsRandomizer {
    private static int seaLevelModifier;
    @Nullable private static BlockState defaultBlockModifier;
    @Nullable private static BlockState defaultLiquidModifier;

    public static Holder<NoiseGeneratorSettings> noiseTransformer(Holder<NoiseGeneratorSettings> implementNoise, MinecraftServer server) {
        NoiseGeneratorSettings noiseToModify = implementNoise.value();
        NoiseGeneratorSettings overworldNoise = server.registryAccess().holderOrThrow(NoiseGeneratorSettings.OVERWORLD).value();
        NoiseRouterRandomizer routerRandomizer = new NoiseRouterRandomizer(noiseToModify,overworldNoise,server);
        getNoiseGenSettingsModifiers(noiseToModify);
        NoiseGeneratorSettings modifiedNoise = new NoiseGeneratorSettings(
                noiseToModify.noiseSettings(),
                defaultBlockModifier == null ? noiseToModify.defaultBlock():defaultBlockModifier,
                defaultLiquidModifier == null ? noiseToModify.defaultBlock():defaultLiquidModifier,
                routerRandomizer.makeNoiseRouter(),
                noiseToModify.surfaceRule(),
                noiseToModify.spawnTarget(),
                noiseToModify.seaLevel() + seaLevelModifier,
                false,
                noiseToModify.aquifersEnabled(),
                noiseToModify.oreVeinsEnabled(),
                noiseToModify.useLegacyRandomSource()
        );
        return Holder.direct(modifiedNoise);
    }

    private static void getNoiseGenSettingsModifiers(NoiseGeneratorSettings settings) {
        List<WorldGenWorldModifier> listOfModifiers = ExcraftDimensionManager.getCurrentManager().getWorldGenModifiers();
        while (!listOfModifiers.isEmpty()) {
            int randomFunctionOrder = ExcraftDimensionManager.getCurrentManagerRandomSource().nextIntBetweenInclusive(1,listOfModifiers.size()) - 1;
            WorldGenWorldModifier worldGenModifier = listOfModifiers.remove(randomFunctionOrder);
            Excraft.LOGGER.info("finding sea level modifier by looking for" + worldGenModifier);
            if (worldGenModifier instanceof NoiseGeneratorSettingsWorldModifier setNoiseGenParameter) {
                Excraft.LOGGER.info("to add to sea level " + setNoiseGenParameter.getSeaLevel());
                seaLevelModifier = setNoiseGenParameter.getSeaLevel();
                defaultBlockModifier = setNoiseGenParameter.defaultBlock();
                defaultLiquidModifier = setNoiseGenParameter.defaultFluid();
            }
        }
    }
}
