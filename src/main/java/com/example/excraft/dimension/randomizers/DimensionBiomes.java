package com.example.excraft.dimension.randomizers;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.dimension.DimensionRandomizer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.io.BinIO;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DimensionBiomes {
    public static BiomeSource buildRandomBiomes(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        Holder<MultiNoiseBiomeSourceParameterList> biomeParameter = registryAccess.holderOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
        BiomeSource biomeSource = multiBiomeSourceFromList(biomeParameter);
        //BiomeSource biomeSource = MultiNoiseBiomeSource.createFromPreset(biomeParameter);
        return biomeSource;
    }

    //TODO: Divide code in a way so that biomes are registered at server start to the list or when config is changed. Randomization is performed from list
    private static BiomeSource multiBiomeSourceFromList(Holder<MultiNoiseBiomeSourceParameterList> biomeParameter) {
        List<Pair<Climate.ParameterPoint,Holder<Biome>>> biomeList = biomeParameter.value().parameters().values();
        List<Pair<Climate.ParameterPoint,Holder<Biome>>> finalList = new ArrayList<>();
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> holderList = new ArrayList<>();
        List<Holder<Biome>> nonGuarenteedList = new ArrayList<>();
        for (Pair<Climate.ParameterPoint,Holder<Biome>> biome: biomeList) {
            Holder<Biome> currentBiome = biome.getSecond();
            if (nonGuarenteedList.contains(currentBiome) || finalList.contains(biome)) {
                continue;
            }
            boolean isInBlacklist = isInConfigList(currentBiome,true);
            boolean isInGuarenteedList = isInConfigList(currentBiome,false);
            Excraft.LOGGER.info("Checking"+ biome.getSecond().getRegisteredName() + " to " + biome.getSecond());
            if (!isInBlacklist && isInGuarenteedList && !Config.DISABLE_GUARENTEED.get()) {
                finalList.addLast(biome);
                Excraft.LOGGER.info("Added "+ biome.getSecond().getRegisteredName() + " to guarenteed list");
            } else if (!isInBlacklist && !isInGuarenteedList) {
                nonGuarenteedList.addLast(currentBiome);
                Excraft.LOGGER.info("Added "+ biome.getSecond().getRegisteredName() + " to normal list");
            }
        }
        for (int i = 1; i < Config.BIOMESTOROLL.get() + 1; i++) {
            int randomNumber = Math.abs(DimensionRandomizer.generateRandomFromSalt().nextInt(nonGuarenteedList.size()));
            Excraft.LOGGER.info("Random Number was selected in biomes to: " + randomNumber);
            Holder<Biome> randomBiome = nonGuarenteedList.remove(randomNumber);
            float[] randomFloatForParameters = new float[7];
            List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameterPointsForThisBiome = biomeList.stream()
                    .filter(pair -> pair.getSecond().equals(randomBiome))
                    .toList();
            Excraft.LOGGER.info("Rolled "+ randomBiome.getRegisteredName() + " as a biome!");
            holderList.addAll(parameterPointsForThisBiome);
            for (Pair<Climate.ParameterPoint, Holder<Biome>> currentTemperature: holderList) {
                Pair<Climate.ParameterPoint, Holder<Biome>> newPair = new Pair<>( Climate.parameters(
                        Climate.Parameter.span(-0.19f,1),
                        Climate.Parameter.span(-0.19f,1),
                        Climate.Parameter.span(-0.19f,1),
                        currentTemperature.getFirst().erosion(),
                        currentTemperature.getFirst().depth(),
                        Climate.Parameter.span(-1,1),
                        currentTemperature.getFirst().offset()),
                        currentTemperature.getSecond()
                );
                finalList.addLast(newPair);
            }
        }
        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(finalList));
    }

    private static boolean isInConfigList(Holder<Biome> biome, boolean isBlackList) {
        if (isBlackList) {
            for (String biomeCheck: Config.BIOME_BLACKLIST.get()) {
                if (biome.is(ResourceLocation.bySeparator(biomeCheck,':'))) {
                    Excraft.LOGGER.info("Comparing From blacklist " + biomeCheck + " with " + biome.getRegisteredName());
                    return true;
                }
            }
        }  else {
            for (String biomeCheck: Config.GUARENTEED_BIOMES.get()) {
                if (biome.is(ResourceLocation.bySeparator(biomeCheck, ':'))) {
                    Excraft.LOGGER.info("Comparing From guarenteed list " + biomeCheck + " with " + biome.getRegisteredName());
                    return true;
                }
            }
        }
        return false;
    }
}