package com.example.excraft.dimension;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class WorldModifierManager {
    public static final ResourceKey<Registry<WorldModifier>> WORLD_MODIFIER_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiers"));
    public static final ResourceKey<Registry<WorldModifierType>> WORLD_MODIFIER_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiertypes"));
    public static final Registry<WorldModifier> WORLD_MODIFIER_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath("excraft", "empty"))
            .create();
    public static final Registry<WorldModifierType> WORLD_MODIFIER_TYPE_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_TYPE_REGISTRY_KEY)
            .sync(true)
            .create();
}
