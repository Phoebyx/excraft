package com.example.excraft.dimension.worldmodifiers.events;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RainWorldModifier extends EventWorldModifierType implements WorldModifier {
    private static final String modifierName = "Rain World";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private final int impact = -4;
    private static boolean active = false;
    private static ResourceKey<Level> levelResourceKey;
    private float currentRainLevel = 0;

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
    public void activateEffect(MinecraftServer server, ResourceKey<Level> level) {
        levelResourceKey = level;
        if (server.overworld().getGameTime() % 6000 == 5999 && !active) {
            active = true;
        } else if (server.overworld().getGameTime() % 6000 == 5999 && active) {
            active = false;
        }
        server.getLevel(level).setRainLevel(rainLevelTarget());
    }

    private float rainLevelTarget() {
            if (!active && currentRainLevel > 0.0F) {
                return currentRainLevel -= 0.02F;
            } else if (active && currentRainLevel < 1.0F) {
                return currentRainLevel += 0.02F;
            } else {return currentRainLevel;}
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> level) {
        server.getLevel(level).setRainLevel(0);
        active = false;
    }

    @SubscribeEvent
    public static void eventListener(PlayerTickEvent.Pre event) {
        if (active && event.getEntity().level().canSeeSky(event.getEntity().blockPosition()) && event.getEntity().level().dimension() == levelResourceKey && event.getEntity().canDrownInFluidType(Fluids.WATER.getFluidType())) {
            event.getEntity().setAirSupply(event.getEntity().getAirSupply() - 7);
        }
    }
}
