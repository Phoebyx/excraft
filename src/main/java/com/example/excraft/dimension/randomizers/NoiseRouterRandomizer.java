package com.example.excraft.dimension.randomizers;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import com.example.excraft.worldgen.densityfunctions.WorldEndDensityFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;

import java.util.List;
import java.util.Objects;

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
    private NoiseGeneratorSettings overworldNoise;

    public NoiseRouterRandomizer(NoiseGeneratorSettings settings,NoiseGeneratorSettings overworldNoise,MinecraftServer server){
        this.currentNoiseRouter = settings.noiseRouter();
        this.server = server;
        this.barrierNoise = DensityFunctions.add(currentNoiseRouter.barrierNoise(),overworldNoise.noiseRouter().barrierNoise());
        this.fluidLevelFloodednessNoise = currentNoiseRouter.fluidLevelFloodednessNoise();
        this.fluidLevelSpreadNoise = currentNoiseRouter.fluidLevelSpreadNoise();
        this.lavaNoise = currentNoiseRouter.lavaNoise();
        this.temperature = currentNoiseRouter.temperature();
        this.vegetation = currentNoiseRouter.vegetation();
        this.continents = DensityFunctions.add(currentNoiseRouter.continents(),overworldNoise.noiseRouter().continents());
        this.erosion = DensityFunctions.add(currentNoiseRouter.erosion(),overworldNoise.noiseRouter().erosion());
        this.depth = DensityFunctions.add(currentNoiseRouter.depth(),overworldNoise.noiseRouter().depth());
        this.ridges = DensityFunctions.add(currentNoiseRouter.ridges(),overworldNoise.noiseRouter().ridges());
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
                modifyFinalDensityFunction(finalDensity),
                modifyFunction(veinToggle),
                modifyFunction(veinRidged),
                modifyFunction(veinGap)
        );
    }
    private DensityFunction modifyFunction(DensityFunction function)  {
        //function = DensityFunctions.rangeChoice(new WorldEndDensityFunction(),0,100000,function,DensityFunctions.constant(-100));
        return function;
    }

    private DensityFunction modifyFinalDensityFunction(DensityFunction function)  {
        DensityFunction modified = applySelectWorldGenModifiers(function);
        //modified = DensityFunctions.rangeChoice(new WorldEndDensityFunction(),0,100000,modified,DensityFunctions.constant(-100));
        return modified;
    }

    private static DensityFunction applySelectWorldGenModifiers(DensityFunction function) {
        List<WorldGenWorldModifier> listOfModifiers = ExcraftDimensionManager.getCurrentManager().getWorldGenModifiers();
        for (int i = 0; i < listOfModifiers.size(); i++) {
            int randomFunctionOrder = ExcraftDimensionManager.getCurrentManagerRandomSource().nextIntBetweenInclusive(1,listOfModifiers.size()) - 1;
            WorldGenWorldModifier worldGenModifier = listOfModifiers.remove(randomFunctionOrder);
            if (Objects.equals(worldGenModifier.getType(), "densityfunction")) {
                function = worldGenModifier.modifyWorld(function);
            }
        }
        return function;
    }


}
