package com.example.excraft.items;

import com.example.excraft.Excraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExcraftItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Excraft.MODID);
    public static final TagKey<Block> INCORRECT_FOR_CACTUS_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("excraft","incorrect_for_cactus_tool"));
    public static final TagKey<Item> CACTUS_REPAIR_BLOCK = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("excraft","cactus_repair_block"));
    public static final DeferredItem<Item> CACTUS_AXE = ITEMS.register("cactaxe", () -> cactusPickAxeDefinition());

    private static Item cactusPickAxeDefinition() {
        return new Item(
                new Item.Properties()
                        .attributes(AxeItem.createAttributes(new SimpleTier(INCORRECT_FOR_CACTUS_TOOL,24,1.5F,1.1F,0, () -> Ingredient.of(CACTUS_REPAIR_BLOCK)),1.5F,0.5F))
        );

    }

}
