package com.example.excraft.dimension.worldmodifiers.world.worldgen;

import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InvertWorldModifier extends WorldWorldModifierType implements WorldGenWorldModifier {
    private static final String modifierName = "Invert";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","invert");
    private final int weight = 1;
    private int impact = -2;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static String type = "densityfunction";

    public String getType() {
        return type;
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
        return impact;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public DensityFunction modifyWorld(DensityFunction toModify) {
        return DensityFunctions.mul(toModify, DensityFunctions.constant(-1));
    }
}
