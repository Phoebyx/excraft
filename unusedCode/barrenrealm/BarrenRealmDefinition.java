package com.example.excraft.barrenrealm;

import com.example.excraft.Excraft;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.HashMap;
import java.util.Map;

public class BarrenRealmDefinition {

    public static final ResourceKey<WorldPreset> BARREN_REALM = ResourceKey.create(Registries.WORLD_PRESET, ResourceLocation.fromNamespaceAndPath(Excraft.MODID,"excraft"));
    public static final ResourceKey<LevelStem> BARREN_REALM_STEM = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(Excraft.MODID,"excraft"));

    public static RegistrySetBuilder registerBarrenRealmsPreset() {
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.WORLD_PRESET,bootstrap -> {
                    bootstrap.register(
                    BARREN_REALM,
                        new WorldPreset(insertLevelStemInMap(bootstrap))
                    );
                });
        return registrySetBuilder;
    }

    private static Map<ResourceKey<LevelStem>, LevelStem> insertLevelStemInMap(BootstrapContext<WorldPreset> context) {
        Map<ResourceKey<LevelStem>, LevelStem> levelStemMap = new HashMap<>();
        levelStemMap.put(BARREN_REALM_STEM,BarrenRealm.barrenRealmLevelStem(context));
        return levelStemMap;
    }

}
