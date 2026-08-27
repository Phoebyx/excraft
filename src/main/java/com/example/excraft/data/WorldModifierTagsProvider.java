package com.example.excraft.data;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class WorldModifierTagsProvider extends TagsProvider<WorldModifier> {
    public WorldModifierTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, WorldModifierRegister.WORLD_MODIFIER_REGISTRY_KEY, lookupProvider, Excraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(WorldModifierRegister.WORLD_MODIFIER_TAG).addOptional(
                ResourceLocation.fromNamespaceAndPath("events","rainworld")
        );
    }
}
