package com.example.excraft.dimension.worldmodifiers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record WorldModifier() {
    private static String modifierName;
    private static WorldModifierType worldModifierType;
    private static ResourceLocation modifierResourceLocation;
    private static ResourceKey modifierResourceKey;
}
