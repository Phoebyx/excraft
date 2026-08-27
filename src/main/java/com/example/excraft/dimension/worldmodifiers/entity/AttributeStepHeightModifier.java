package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttributeStepHeightModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Step Height";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","stepheight");
    private final int weight = 1;
    private final int impact = 4;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static AttributeModifier stepHeight = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","stepheight"),2.0F,AttributeModifier.Operation.ADD_VALUE);

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
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
        for (Player player:server.getPlayerList().getPlayers()) {
            attributeToRemove(player);
        }
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        LivingEntity entity;
        if (event.getLevel().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL && active && event.getEntity() instanceof LivingEntity) {
            entity = (LivingEntity) event.getEntity();
            attributeToModify(entity);
        } else if ((event.getLevel().dimension() != ExcraftDimensionManager.EXCRAFT_LEVEL || !active) && event.getEntity() instanceof Player) {
            entity = (LivingEntity) event.getEntity();
            attributeToRemove(entity);
        }
    }

    private static void attributeToRemove(LivingEntity entity) {
        entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(stepHeight);
    }

    private static void attributeToModify(LivingEntity entity) {
        entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(stepHeight);
    }
}
