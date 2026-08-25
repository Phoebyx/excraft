package com.example.excraft.dimension.worldmodifiers.events;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.WorldModifierRegister;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class RainWorldModifier extends EventWorldModifierType implements WorldModifier {
    String modifierName = "Rain World";
    ResourceLocation modifierResourceLocation = null;
    public static final Supplier<WorldModifier> RAINWORLD = WorldModifierRegister.EVENT.register("rainworld", RainWorldModifier::new);
    private final int weight = 1;
    private final int impact = -4;
    private static boolean active;
    private final Boolean cycle = true;
    private static ResourceKey<Level> levelResourceKey;
    private static int tickUnderWeather;
    private static DamageSource drowningSource;

    @Override
    public int getWeight() {
        return weight;
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
        server.getLevel(level).setRainLevel(0.8F);
        DamageSources getDamageSources = new DamageSources(server.registryAccess());
        drowningSource = getDamageSources.drown();
        active = true;
        levelResourceKey = level;
        Excraft.LOGGER.info("Activated RainWorld modifier");
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> level) {
        server.getLevel(level).setRainLevel(0);
        active = false;
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (active && event.getEntity().level().canSeeSky(event.getEntity().blockPosition()) && event.getEntity().level().dimension() == levelResourceKey && event.getEntity().canDrownInFluidType(Fluids.WATER.getFluidType())) {
            tickUnderWeather++;
            Excraft.LOGGER.info("Drowning Player" + tickUnderWeather);
            if (tickUnderWeather >= 10) {
                tickUnderWeather = 0;
                event.getEntity().hurt(drowningSource,0.5F);
                Excraft.LOGGER.info("Air supply reduced" + event.getEntity().getAirSupply());
            }
        } else { tickUnderWeather = 0;}
    }
}
