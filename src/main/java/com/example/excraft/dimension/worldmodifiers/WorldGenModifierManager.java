package com.example.excraft.dimension.worldmodifiers;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noisesettings.NoiseGeneratorSettingsWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.SpecialWorldGenModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;

public class WorldGenModifierManager {
    private static SpecialWorldGenModifier specialModifier;
    public static ChunkAccess applySpecialWorldShapeWorldGenModifiers(ChunkAccess chunk) {
        if (specialModifier == null) {return chunk;}
        specialModifier.chunkGenerator(chunk);
        return chunk;
    }

    public static void getSpecialModifier() {
        List<WorldGenWorldModifier> listOfModifiers = ExcraftDimensionManager.getCurrentManager().getWorldGenModifiers();
        while (!listOfModifiers.isEmpty()) {
            int randomFunctionOrder = ExcraftDimensionManager.getCurrentManagerRandomSource().nextIntBetweenInclusive(1,listOfModifiers.size()) - 1;
            WorldGenWorldModifier worldGenModifier = listOfModifiers.remove(randomFunctionOrder);
            if (worldGenModifier instanceof SpecialWorldGenModifier specialWorldGenModifier) {
                specialModifier = specialWorldGenModifier;
                return;
            }
        }
    }
}
//for (WorldGenWorldModifier worldGenModifers:getFilteredNoiseList();)