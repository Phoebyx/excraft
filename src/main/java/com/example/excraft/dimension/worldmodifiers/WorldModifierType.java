package com.example.excraft.dimension.worldmodifiers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface WorldModifierType {
    String worldModifierType = "";
    ResourceLocation modifierTypeResourceLocation = null;
    ResourceKey modifierTypeResourceKey = null;
    DeferredRegister<WorldModifierType> WORLD_MODIFIER_TYPE_DEFERRED_REGISTER = null;

    public String getModifierName();
}
