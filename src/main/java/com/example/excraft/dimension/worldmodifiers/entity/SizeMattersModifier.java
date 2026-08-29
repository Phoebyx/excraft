package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SizeMattersModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Size Matters";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","sizematters");
    private final int weight = 1;
    private final int impact = 4;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static boolean impactDirection = false;
    private static int affectedEntity = 0; //0 - Players Only //1 - Other Entities Only // 2 - Both
    private static AttributeModifier sizeMattersSmall = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","sizematterssmall"),-0.5F,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static AttributeModifier sizeMattersBig = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("excraft","sizemattersbig"),1.0F,AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public SizeMattersModifier() {
        rollImpact();
    }

    @Override
    public int getWeight() {
        return weight;
    }

    private static void rollImpact() {
        impactDirection = ExcraftDimensionManager.getCurrentManagerRandomSource().nextBoolean();
        affectedEntity = ExcraftDimensionManager.getCurrentManagerRandomSource().nextIntBetweenInclusive(0,2);
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
    public static void eventListener(EntityJoinLevelEvent event) {
        LivingEntity entity;
        if (event.getLevel().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL && active && event.getEntity() instanceof LivingEntity) {
            entity = (LivingEntity) event.getEntity();
            if (whoToModify(event.getEntity())) {
                attributeToModify(entity);
            }
        } else if ((event.getLevel().dimension() != ExcraftDimensionManager.EXCRAFT_LEVEL || !active) && event.getEntity() instanceof Player) {
            entity = (LivingEntity) event.getEntity();
            attributeToRemove(entity);
        }
    }

    private static boolean whoToModify(Entity entity) {
        switch (affectedEntity) {
            case (0): {return entity instanceof Player;}
            case (1): {return !(entity instanceof Player);}
            case (2): {return true;}
        }
        return false;
    }

    private static void attributeToRemove(LivingEntity entity) {
        AttributeModifier modifier;
        if (impactDirection) {modifier = sizeMattersSmall;} else {modifier = sizeMattersBig;}
        entity.getAttribute(Attributes.SCALE).removeModifier(modifier);
    }

    private static void attributeToModify(LivingEntity entity) {
        AttributeModifier modifier;
        if (impactDirection) {modifier = sizeMattersSmall;} else {modifier = sizeMattersBig;}
        entity.getAttribute(Attributes.SCALE).addOrReplacePermanentModifier(modifier);
    }
}
