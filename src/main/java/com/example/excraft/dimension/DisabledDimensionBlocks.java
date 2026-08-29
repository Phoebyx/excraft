package com.example.excraft.dimension;

import com.example.excraft.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

public class DisabledDimensionBlocks {
    @SubscribeEvent
    public static void preventDisabledBlocks(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() != event.getLevel().getServer().overworld().getLevel()) {
            blockInDisabledBlockList(event);
        }
    }
    @SubscribeEvent
    private static void playerBlockPlaceFix(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().dimension() != Level.OVERWORLD) {
            disableBlockPlaceForPlayer(event);
        }
    }

    private static void blockInDisabledBlockList(BlockEvent.EntityPlaceEvent event) {
        List<String> list = (List<String>) Config.DISABLED_WORKSTATIONS_IN_DIMENSION.get();
        if (event.getLevel().isClientSide()) {return;}
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
    private static void disableBlockPlaceForPlayer(PlayerInteractEvent.RightClickBlock event) {
        List<String> list = (List<String>) Config.DISABLED_WORKSTATIONS_IN_DIMENSION.get();
        if (!event.getLevel().isClientSide()) {return;}
        for (String current:list) {
            if (event.getItemStack().getItem() instanceof BlockItem blockItem) {
                try {
                    TagKey<Block> tag = TagKey.create(Registries.BLOCK, returnNamespaceAndPath(current));
                    if (blockItem.getBlock().defaultBlockState().is(tag)) {
                        event.setCanceled(true);
                        return;
                    }
                } catch (Exception ignored) {
                }
                if (blockItem.getBlock().toString().equals("Block{" + current + "}")) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private static ResourceLocation returnNamespaceAndPath(String current) {
        String[] separated = current.split(":");
        return ResourceLocation.fromNamespaceAndPath(separated[0],separated[1]);
    }
}