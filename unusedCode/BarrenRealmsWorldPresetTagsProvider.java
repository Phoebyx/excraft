package com.example.excraft.data;

import com.example.excraft.barrenrealm.BarrenRealm;
import com.example.excraft.barrenrealm.BarrenRealmDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;


import java.util.concurrent.CompletableFuture;

public class BarrenRealmsWorldPresetTagsProvider extends WorldPresetTagsProvider {
    public BarrenRealmsWorldPresetTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(output,lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<WorldPreset> presets =
                provider.lookupOrThrow(Registries.WORLD_PRESET);
        tag(WorldPresetTags.NORMAL).add(BarrenRealmDefinition.BARREN_REALM);
    }
}
