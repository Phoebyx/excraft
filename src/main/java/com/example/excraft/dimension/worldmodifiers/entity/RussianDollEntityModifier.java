package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RussianDollEntityModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Russian Doll";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","russiandoll");
    private final int weight = 1;
    private final int impact = -7;
    private static boolean active = false;
    private static AttributeModifier russiandollModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","russiandoll"),-0.5F,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Override
    public int getWeight() {
        return 1;
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
    }

    @SubscribeEvent
    public static void eventListener(LivingDeathEvent event) {
        if (eventListenerBool(event) && !event.getEntity().getAttribute(Attributes.SCALE).hasModifier(russiandollModifier.id())) {
            EntityType<?> type = event.getEntity().getType();
            eventListenerEntityTransformer(type,event,-0.2F);
            eventListenerEntityTransformer(type,event,0.2F);
        }
    }
    @SubscribeEvent
    public static void eventListenerForDrops(LivingDropsEvent event) {
        if(active && event.getEntity().level().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL && !(event.getEntity() instanceof Player)
                && !event.getEntity().getAttribute(Attributes.SCALE).hasModifier(russiandollModifier.id())) {
            event.setCanceled(true);
        }
    }

    private static boolean eventListenerBool(LivingDeathEvent event) {
        return active
                && event.getEntity().level().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL
                && !(event.getEntity() instanceof Player);
    }
    private static void eventListenerEntityTransformer(EntityType<?> type, LivingDeathEvent event,float offset) {
        LivingEntity entity = (LivingEntity) type.create(event.getEntity().level());
        entity.moveTo(event.getEntity().getX() + offset,event.getEntity().getY(),event.getEntity().getZ() + offset);
        event.getEntity().level().addFreshEntity(entity);
        entity.getAttribute(Attributes.SCALE).addOrReplacePermanentModifier(russiandollModifier);
        entity.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(russiandollModifier);
    }
}
