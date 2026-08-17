package com.example.excraft.blocks;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DisabledDimensionBlocksTag extends BlockTagsProvider {
    // Get parameters from GatherDataEvent.
    public DisabledDimensionBlocksTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Excraft.MODID, existingFileHelper);
    }
    public static final TagKey<Block> DISABLEDINDIMENSION = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("excraft", "disabledindimension")
    );

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        List<ResourceLocation> resourceLocationsList = returnNamespaceAndPath();
        for (ResourceLocation resourceLocation: resourceLocationsList) {
            tag(DISABLEDINDIMENSION)
                    .addOptional(resourceLocation)
                    .addOptionalTag(resourceLocation)
            ;
            Excraft.LOGGER.info("Added to tag" + resourceLocation.getPath());
        }
    }
    private List<ResourceLocation> returnNamespaceAndPath() {
        List<ResourceLocation> validBlocks = new ArrayList<>();
        for (String toBeValidated : Config.DISABLED_WORKSTATIONS_IN_DIMENSION.get()) {
            String[] separated = toBeValidated.split(":");
            validBlocks.addLast(ResourceLocation.fromNamespaceAndPath(separated[0],separated[1]));
        }
        return validBlocks;
    }
}

