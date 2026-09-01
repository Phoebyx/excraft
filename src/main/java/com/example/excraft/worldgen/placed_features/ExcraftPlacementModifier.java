package com.example.excraft.worldgen.placed_features;

import com.example.excraft.Excraft;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class ExcraftPlacementModifier extends PlacementModifier {

    public static final MapCodec<ExcraftPlacementModifier> CODEC =
            MapCodec.unit(ExcraftPlacementModifier::new);

    public static final ExcraftPlacementModifier INSTANCE =
            new ExcraftPlacementModifier();

    private ExcraftPlacementModifier() {}

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos) {
        return Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() {
        return null;
    }
}
