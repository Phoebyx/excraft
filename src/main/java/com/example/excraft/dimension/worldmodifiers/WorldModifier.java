package com.example.excraft.dimension.worldmodifiers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface WorldModifier {
    String modifierName = " ";
    WorldModifierType worldModifierType = null;
    String modifierResourceLocation = null;
    ResourceKey modifierResourceKey = null;
    int weight = 0;
    int impact = 0;
    boolean active = false;
    List<String> dependentOn = null;
    TagKey<WorldModifier> WORLD_MODIFIER_TAG_KEY = null;

    default public int getWeight() {return weight;};

    public String getModifierName();

    public String getModifierResourceLocation();

    default public int getImpact() {return impact;};

    public @Nullable List<String> getDependentOn();

    public void activateEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey);

    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey);

}
