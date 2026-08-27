package com.example.excraft.blocks;

import com.example.excraft.items.ExcraftItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExcraftBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("excraft");
    public static final DeferredBlock<Block> UNBREAKABLE_SANDSTONE = register("unbreakable_sandstone", () -> unbreakableSandstoneDefinition());
    public static final DeferredBlock<StairBlock> UNBREAKABLE_SANDSTONE_STAIRS = register("unbreakable_sandstone_stairs", () -> unbreakableSandstoneStairsDefinition());
    public static final DeferredBlock<SlabBlock> UNBREAKABLE_SANDSTONE_SLABS = register("unbreakable_sandstone_slab", () -> unbreakableSandstoneSlabBlockDefinition());
    public static final DeferredBlock<Block> UNBREAKABLE_CHISELED_SANDSTONE = register("unbreakable_chiseled_sandstone", () -> unbreakableChiseledSandstoneDefinition());
    public static final DeferredBlock<ExcraftPortalBlock> EXCRAFT_PORTAL = BLOCKS.register("excraft_portal", () -> excraftPortalBlockDefinition());

    private static Block unbreakableSandstoneDefinition() {
        return new Block(
                BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
                        .destroyTime(-1)
                        .explosionResistance(3600000)
                        .sound(SoundType.STONE)
                        .friction(0.6F)
        );
    }
    private static StairBlock unbreakableSandstoneStairsDefinition() {
        return new StairBlock(
                Blocks.SANDSTONE_STAIRS.defaultBlockState(),
                Block.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)
                        .destroyTime(-1)
                        .explosionResistance(3600000)
        );
    }
    private static SlabBlock unbreakableSandstoneSlabBlockDefinition() {
        return new SlabBlock(
                Block.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)
                        .destroyTime(-1)
                        .explosionResistance(3600000)
        );
    }
    private static Block unbreakableChiseledSandstoneDefinition() {
        return new Block(
                BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_SANDSTONE)
                        .destroyTime(-1)
                        .explosionResistance(3600000)
                        .sound(SoundType.STONE)
                        .friction(0.6F)
        );
    }
    private static Block unbreakableBeaconChiseledSandstoneDefinition() {
        return new Block(
                BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_SANDSTONE)
                        .destroyTime(-1)
                        .explosionResistance(3600000)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> 7)
                        .friction(0.6F)
        );
    }
    private static ExcraftPortalBlock excraftPortalBlockDefinition() {
        return new ExcraftPortalBlock(BlockBehaviour.Properties.of()
                .destroyTime(-1)
                .explosionResistance(3600000)
                .lightLevel(state -> 11)
                .noCollission()
                .pushReaction(PushReaction.BLOCK)
                .sound(SoundType.GLASS)
                .randomTicks()
                .forceSolidOn()
        );
    }

    private static <T extends Block> DeferredBlock<T> baseRegister(String name, Supplier<? extends T> block, Function<DeferredBlock<T>, Supplier<? extends Item>> item) {
        DeferredBlock<T> register = BLOCKS.register(name, block);
        ExcraftItems.ITEMS.register(name, item.apply(register));
        return register;
    }

    private static <B extends Block> DeferredBlock<B> register(String name, Supplier<B> block) {
        return baseRegister(name, block, ExcraftBlocks::registerBlockItem);
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final DeferredBlock<T> deferredBlock) {
        return () -> {
            DeferredBlock<T> block = Objects.requireNonNull(deferredBlock);
            return new BlockItem(block.get(), new Item.Properties());
        };
    }
}
