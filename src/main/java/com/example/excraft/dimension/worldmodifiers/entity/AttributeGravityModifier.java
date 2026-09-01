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

public class AttributeGravityModifier extends EntityWorldModifierType implements WorldModifier {
    public static final String modifierName = "Gravity";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private final int impact = 5;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static boolean impactDirection = false;
    private static AttributeModifier modifierPositive = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitynegative"),-0.8F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierNegative = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitypositive"),0.5F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierNegativeFall = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitypositivefall"),-0.5F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierPositiveFall = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitynegativefall"),1.5F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public AttributeGravityModifier() {
        rollImpact();
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
        if (impactDirection) {return impact;} else {return -impact;}
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
        rollImpact();
    }

    @SubscribeEvent
    public static void eventListener(EntityJoinLevelEvent event) {
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
        AttributeModifier modifier;
        AttributeModifier modifierFall;
        AttributeModifier modifierFallMultiplier;
        if (impactDirection) {modifier = modifierPositive; modifierFall = modifierPositiveFall; modifierFallMultiplier = modifierNegativeFall;}
        else {modifier = modifierNegative; modifierFall = modifierNegativeFall; modifierFallMultiplier = modifierPositiveFall;}
        entity.getAttribute(Attributes.GRAVITY).removeModifier(modifier);
        entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(modifierFall);
        entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(modifierFallMultiplier);
    }

    private static void attributeToModify(LivingEntity entity) {
        AttributeModifier modifier;
        AttributeModifier modifierFall;
        AttributeModifier modifierFallMultiplier;
        if (impactDirection) {modifier = modifierPositive; modifierFall = modifierPositiveFall; modifierFallMultiplier = modifierNegativeFall;}
        else {modifier = modifierNegative; modifierFall = modifierNegativeFall; modifierFallMultiplier = modifierPositiveFall;}
        entity.getAttribute(Attributes.GRAVITY).addOrReplacePermanentModifier(modifier);
        entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrReplacePermanentModifier(modifierFall);
        entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addOrReplacePermanentModifier(modifierFallMultiplier);
    }
    private static void rollImpact() {
        impactDirection = ExcraftDimensionManager.getCurrentManagerRandomSource().nextBoolean();
    }
}
