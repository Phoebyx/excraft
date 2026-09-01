package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.List;
import java.util.Vector;

public class InfiniteEnderPearlModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Infinite Ender Pearl";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private final int impact = 5;
    private static boolean active = false;

    @Override
    public int getWeight() {
        return weight;
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
    public static void eventListener(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity() instanceof Player player && active) {
            Vec3 vector3d = player.getLookAngle().scale(1.2);
            Entity enderPearl = new ThrownEnderpearl(player.level(), player);
            player.level().addFreshEntity(enderPearl);
            enderPearl.setPos(player.getEyePosition());
            enderPearl.push(vector3d);
        }
    }
}
