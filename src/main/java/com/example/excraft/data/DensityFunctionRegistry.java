package com.example.excraft.data;

import com.example.excraft.Excraft;
import com.example.excraft.worldgen.densityfunctions.WorldEndDensityFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DensityFunctionRegistry {

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister
            .create(Registries.DENSITY_FUNCTION_TYPE,Excraft.MODID);

    public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<WorldEndDensityFunction>> WORLD_END = DENSITY_FUNCTION_TYPES
            .register("worldend", WorldEndDensityFunction.CODEC::codec);

}
