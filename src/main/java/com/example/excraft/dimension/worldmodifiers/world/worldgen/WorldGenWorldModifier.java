package com.example.excraft.dimension.worldmodifiers.world.worldgen;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.world.level.levelgen.Density;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.Nullable;

public interface WorldGenWorldModifier extends WorldModifier {
    public DensityFunction modifyWorld(DensityFunction function);

    public String getType();

}
