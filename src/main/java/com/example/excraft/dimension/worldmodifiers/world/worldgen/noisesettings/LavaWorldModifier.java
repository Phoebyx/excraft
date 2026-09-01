package com.example.excraft.dimension.worldmodifiers.world.worldgen.noisesettings;

import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LavaWorldModifier extends WorldWorldModifierType implements NoiseGeneratorSettingsWorldModifier, WorldGenWorldModifier {
    private static final String modifierName = "Lava World";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = -7;
    private static ResourceKey<Level> levelResourceKey;
    private static String type = "noisegeneratorsettings";

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
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public BlockState defaultFluid() {
        return Blocks.LAVA.defaultBlockState();
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
