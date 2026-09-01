package com.example.excraft.dimension.worldmodifiers.world.worldgen.noisesettings;

import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.world.level.block.state.BlockState;

public interface NoiseGeneratorSettingsWorldModifier extends WorldGenWorldModifier {
    public int getSeaLevel();
    public BlockState defaultFluid();
    public BlockState defaultBlock();
}
