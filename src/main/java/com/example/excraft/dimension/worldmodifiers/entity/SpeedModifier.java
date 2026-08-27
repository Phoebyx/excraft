package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.commoble.infiniverse.internal.DimensionManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Speed";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","speed");
    private final int weight = 1;
    private final int impact = 5;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;

    @Override
    public int getWeight() {
        return weight;
    }

    public String getModifierName() {
        return modifierName;
    }

    @Override
    public ResourceLocation getModifierResourceLocation() {
        return modifierResourceLocation;
    }

    @Override
    public int getImpact() {
        return impact;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public void activateEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = true;
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = false;
    }

    @SubscribeEvent
    public static void playerTick(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity().level().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL && active) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,99999999,1));
        }
        if (event.getEntity().level().dimension() != ExcraftDimensionManager.EXCRAFT_LEVEL && active) {
            event.getEntity().removeEffect(MobEffects.MOVEMENT_SPEED);
        }
    }
}
