package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.Excraft;
import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionEffectModifier extends EntityWorldModifierType implements WorldModifier {
    private final String modifierName;
    private final String modifierResourceLocation;
    private final int weight = 1;
    private final int impact;
    private boolean active;
    private final Holder<MobEffect> effect;
    private final int level;

    public PotionEffectModifier(int impact, String name, Holder<MobEffect> mobEffect,int level) {
        this.modifierName = name;
        this.impact = impact;
        this.effect = mobEffect;
        this.level = level;
        this.active = false;
        this.modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public String getModifierName() {
        return modifierName;
    }

    @Override
    public String getModifierResourceLocation() {
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
    public void eventListener(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity().level().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL && this.active && event.getEntity().getServer() != null) {
            event.getEntity().addEffect(new MobEffectInstance(effect, (int) ExcraftTimer.getCurrentTimer(event.getEntity().getServer()),level));
        }
        if (event.getEntity().level().dimension() != ExcraftDimensionManager.EXCRAFT_LEVEL && active) {
            event.getEntity().removeEffect(effect);
        }
    }
}
