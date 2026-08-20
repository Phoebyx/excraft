package com.example.excraft.dimension.randomizers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.function.Function;

public class DensityFunctionOffset implements DensityFunction {
    private final DensityFunction.NoiseHolder noise;
    private final double offset;
    static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(DensityFunctions.DIRECT_CODEC.fieldOf("offset")) ;
    public DensityFunctionOffset(DensityFunction.NoiseHolder noise, double offset) {
        this.noise = noise;
        this.offset = offset;
    }

    @Override
    public double compute(FunctionContext functionContext) {
        return this.noise.getValue((double) functionContext.blockX() + offset, functionContext.blockY(),functionContext.blockZ() + offset);
    }

    @Override
    public void fillArray(double[] array, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(array, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new DensityFunctionOffset(visitor.visitNoise(this.noise),this.offset));
    }

    @Override
    public double minValue() {
        return -this.maxValue();
    }

    @Override
    public double maxValue() {
        return this.noise.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return DensityFunctions.constant(3).codec();
    }
}


