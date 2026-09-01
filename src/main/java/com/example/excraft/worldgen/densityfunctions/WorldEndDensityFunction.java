package com.example.excraft.worldgen.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record WorldEndDensityFunction() implements DensityFunction.SimpleFunction {

    public static final KeyDispatchDataCodec<WorldEndDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new WorldEndDensityFunction()));
    @Override
    public double compute(FunctionContext functionContext) {
        if ((functionContext.blockX() * functionContext.blockX() + functionContext.blockZ() * functionContext.blockZ() - 3 * functionContext.blockY() - 10) < 0.003F * functionContext.blockY()* functionContext.blockY()* functionContext.blockY()) {
            return 1;
        } else return -10000;
    }

    @Override
    public double minValue() {
        return -30_000_000;
    }

    @Override
    public double maxValue() {
        return 30_000_000;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
