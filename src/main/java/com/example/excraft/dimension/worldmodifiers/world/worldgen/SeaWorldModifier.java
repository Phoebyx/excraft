package com.example.excraft.dimension.worldmodifiers.world.worldgen;

import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeaWorldModifier extends WorldWorldModifierType implements NoiseGeneratorSettingsWorldModifier,WorldGenWorldModifier {
    private static final String modifierName = "Sea World";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","seaworld");
    private final int weight = 1;
    private int impact = -5;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean invert = false;
    private static String type = "noisegeneratorsettings";

    @Override
    public void roll() {
        invert = ExcraftDimensionManager.getCurrentManagerRandomSource().nextBoolean();
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
        if (invert) {return impact;}
        else return 1;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public int getSeaLevel() {
        roll();
        if (invert) {return ExcraftDimensionManager.getCurrentManagerRandomSource().nextInt(20,150);}
        else return -ExcraftDimensionManager.getCurrentManagerRandomSource().nextInt(20,150);
    }

    @Override
    public BlockState defaultFluid() {
        return null;
    }

    @Override
    public BlockState defaultBlock() {
        return null;
    }

    @Override
    public String getType() {
        return type;
    }
}
