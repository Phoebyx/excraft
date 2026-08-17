package com.example.excraft.dimension;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.blocks.DisabledDimensionBlocksTag;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.apache.logging.log4j.EventLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DisabledDimensionBlocks {
    @SubscribeEvent
    public static void preventDisabledBlocks(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() != event.getLevel().getServer().overworld().getLevel()) {
            blockInDisabledBlockList(event);
        }
    }

    private static void blockInDisabledBlockList(BlockEvent.EntityPlaceEvent event) {
        List<String> list = (List<String>) Config.DISABLED_WORKSTATIONS_IN_DIMENSION.get();
        for (String current:list) {
            try {
                TagKey<Block> tag = TagKey.create(Registries.BLOCK, returnNamespaceAndPath(current));
                if (event.getPlacedBlock().is(tag)) {
                    event.setCanceled(true);
                    return;
                }
            } catch (Exception ignored) {}
            if (event.getPlacedBlock().getBlock().toString().equals("Block{"+ current +"}")) {
                event.setCanceled(true);
                return;
            }
        }
    }

    private static ResourceLocation returnNamespaceAndPath(String current) {
        String[] separated = current.split(":");
        return ResourceLocation.fromNamespaceAndPath(separated[0],separated[1]);
    }
}