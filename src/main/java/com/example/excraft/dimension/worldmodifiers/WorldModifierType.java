package com.example.excraft.dimension.worldmodifiers;

import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.dimension.WorldModifierManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Supplier;

public interface WorldModifierType {
    String worldModifierType = "";
    ResourceLocation modifierTypeResourceLocation = null;
    ResourceKey modifierTypeResourceKey = null;
    DeferredRegister<WorldModifierType> WORLD_MODIFIER_TYPE_DEFERRED_REGISTER = null;

    public String getModifierName();
}
