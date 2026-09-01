package com.example.excraft.dimension.worldmodifiers.world;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PermanentTimeOfDayModifier extends WorldWorldModifierType implements WorldModifier {
    private static final String modifierName = "Permanent Time";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = 6;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static long time = -1;

    public PermanentTimeOfDayModifier() {
        rollTime();
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
        if (time == -1) {
            rollTime();
        }
        server.overworld().setDayTime(time);
        server.overworld().setDayTimePerTick(-0.01F);
    }

    private void rollTime() {
        int randomInt = ExcraftDimensionManager.getCurrentManagerRandomSource().nextIntBetweenInclusive(0,3);
        switch (randomInt) {
            case (0): {
                time = 6000;
                impact = 3;
                break;
            } case (1): {
                time = 23500;
                impact = 1;
                break;
            } case (2): {
                time = 18000;
                impact = -3;
                break;
            }
        }
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = false;
        rollTime();
    }
}
