package com.example.excraft.dimension.randomizers;

import com.example.excraft.dimension.DimensionRandomizer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.NoiseData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseRouterRandomizer {
    private NoiseRouter currentNoiseRouter;
    private DensityFunction barrierNoise;
    private DensityFunction fluidLevelFloodednessNoise;
    private DensityFunction fluidLevelSpreadNoise;
    private DensityFunction lavaNoise;
    private DensityFunction temperature;
    private DensityFunction vegetation;
    private DensityFunction continents;
    private DensityFunction erosion;
    private DensityFunction depth;
    private DensityFunction ridges;
    private DensityFunction initialDensityWithoutJaggedness;
    private DensityFunction finalDensity;
    private DensityFunction veinToggle;
    private DensityFunction veinRidged;
    private DensityFunction veinGap;
    private MinecraftServer server;

    public NoiseRouterRandomizer(NoiseGeneratorSettings settings,MinecraftServer server){
        this.currentNoiseRouter = settings.noiseRouter();
        this.server = server;
        this.barrierNoise = currentNoiseRouter.barrierNoise();
        this.fluidLevelFloodednessNoise = currentNoiseRouter.fluidLevelFloodednessNoise();
        this.fluidLevelSpreadNoise = currentNoiseRouter.fluidLevelSpreadNoise();
        this.lavaNoise = currentNoiseRouter.lavaNoise();
        this.temperature = currentNoiseRouter.temperature();
        this.vegetation = currentNoiseRouter.vegetation();
        this.continents = currentNoiseRouter.continents();
        this.erosion = currentNoiseRouter.erosion();
        this.depth = currentNoiseRouter.depth();
        this.ridges = currentNoiseRouter.ridges();
        this.initialDensityWithoutJaggedness = currentNoiseRouter.initialDensityWithoutJaggedness();
        this.finalDensity = currentNoiseRouter.finalDensity();
        this.veinToggle = currentNoiseRouter.veinToggle();
        this.veinRidged = currentNoiseRouter.veinRidged();
        this.veinGap = currentNoiseRouter.veinGap();

    }
    public NoiseRouter makeNoiseRouter() {
        return new NoiseRouter(
                modifyFunction(barrierNoise),
                modifyFunction(fluidLevelFloodednessNoise),
                modifyFunction(fluidLevelSpreadNoise),
                modifyFunction(lavaNoise),
                modifyFunction(temperature),
                modifyFunction(vegetation),
                modifyFunction(continents),
                modifyFunction(erosion),
                modifyFunction(depth),
                modifyFunction(ridges),
                modifyFunction(initialDensityWithoutJaggedness),
                modifyFunction(finalDensity),
                modifyFunction(veinToggle),
                modifyFunction(veinRidged),
                modifyFunction(veinGap)
        );
    }
    private DensityFunction modifyFunction(DensityFunction function)  {
        /* double d1 = 1/DimensionRandomizer.generateRandomFromSalt().nextDouble();
        double d2 = 1/DimensionRandomizer.generateRandomFromSalt().nextDouble();
        return DensityFunctions.mul(DensityFunctions.yClampedGradient(-64,384, d1, d2),function);*/
        double d2 = DimensionRandomizer.generateRandomFromSalt().nextDouble();
        NormalNoise noise = NormalNoise.create(DimensionRandomizer.generateRandomFromSalt(),-6,1,1,1);
        return function;
    }
}
