package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionMartidomEntityModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Potion Martidom";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private final int impact = -5;
    private static boolean active = false;

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
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
    public static void eventListener(EntityLeaveLevelEvent event) {
        if (eventListenerBool(event) && event.getEntity() instanceof LivingEntity entity) {
            Entity liveTNT = new PrimedTnt(entity.level(),entity.getX(),entity.getY(),entity.getZ(),entity);
            event.getLevel().addFreshEntity(liveTNT);
        }
    }

    private static boolean eventListenerBool(EntityLeaveLevelEvent event) {
        return active
                && event.getEntity().getRemovalReason() == Entity.RemovalReason.KILLED
                && event.getLevel().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL;
    }
}
