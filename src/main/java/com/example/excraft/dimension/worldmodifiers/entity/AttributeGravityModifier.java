package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttributeGravityModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Gravity";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","gravity");
    private final int weight = 1;
    private final int impact = 5;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static boolean impactDirection = false;
    private static AttributeModifier modifierPositive = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitynegative"),-0.8F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierNegative = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitypositive"),0.5F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierNegativeFall = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitypositivefall"),-0.9F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static AttributeModifier modifierPositiveFall = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","gravitynegativefall"),3.0F,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public AttributeGravityModifier() {
        rollImpact();
    }

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
        AttributeModifier modifier;
        AttributeModifier modifierFall;
        AttributeModifier modifierFallMultiplier;
        if (impactDirection) {modifier = modifierPositive; modifierFall = modifierPositiveFall; modifierFallMultiplier = modifierNegativeFall;} else {modifier = modifierNegative; modifierFall = modifierNegativeFall; modifierFallMultiplier = modifierPositiveFall;}
        entity.getAttribute(Attributes.GRAVITY).removeModifier(modifier);
        entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(modifierFall);
        entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(modifierFallMultiplier);
    }

    private static void attributeToModify(LivingEntity entity) {
        AttributeModifier modifier;
        AttributeModifier modifierFall;
        AttributeModifier modifierFallMultiplier;
        if (impactDirection) {modifier = modifierPositive; modifierFall = modifierPositiveFall; modifierFallMultiplier = modifierNegativeFall;} else {modifier = modifierNegative; modifierFall = modifierNegativeFall; modifierFallMultiplier = modifierPositiveFall;}
        entity.getAttribute(Attributes.GRAVITY).addOrUpdateTransientModifier(modifier);
        entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrUpdateTransientModifier(modifierFall);
        entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addOrUpdateTransientModifier(modifierFallMultiplier);
    }
    private static void rollImpact() {
        impactDirection = DimensionRandomizer.generateRandomFromSalt().nextBoolean();
    }
}
