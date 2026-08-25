package com.example.excraft.dimension;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.events.RainWorldModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class WorldModifierRegister {
    public static final ResourceKey<Registry<WorldModifier>> WORLD_MODIFIER_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiers"));
    public static final ResourceKey<Registry<WorldModifierType>> WORLD_MODIFIER_TYPE_REGISTRY_KEY = net.minecraft.resources.ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiertypes"));
    public static final Registry<WorldModifier> WORLD_MODIFIER_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath("excraft", "empty"))
            .create();
    public static final Registry<WorldModifierType> WORLD_MODIFIER_TYPE_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_TYPE_REGISTRY_KEY)
            .sync(true)
            .create();
    public static final DeferredRegister<WorldModifier> EVENT = DeferredRegister.create(WORLD_MODIFIER_REGISTRY, "events");
    public static final DeferredRegister<WorldModifier> WORLD = DeferredRegister.create(WORLD_MODIFIER_REGISTRY, "world");
    public static final DeferredRegister<WorldModifier> ENTITY = DeferredRegister.create(WORLD_MODIFIER_REGISTRY, "entity");

    public static void registerModifierEventListeners() {
        NeoForge.EVENT_BUS.addListener(RainWorldModifier::playerTick);
    }
}
