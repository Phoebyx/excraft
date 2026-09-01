package com.example.excraft.dimension.worldmodifiers.world.worldgen.special;

import com.example.excraft.dimension.SolidWorldBorderCreator;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkyGridWorldModifier extends WorldWorldModifierType implements SpecialWorldGenModifier, WorldGenWorldModifier {
    private static final String modifierName = "Sky Grid World";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = 0;
    private static boolean active = false;
    private static String type = "specialchunkgenerator";

    @Override
    public String getModifierName() {
        return modifierName;
    }

    @Override
    public int getImpact() {
        return impact;
    }
    @Override
    public int getWeight() {
        return weight;
    }
    @Override
    public String getModifierResourceLocation() {
        return modifierResourceLocation;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public void chunkGenerator(ChunkAccess chunk) {
        BlockState blockState = Blocks.AIR.defaultBlockState();
        int blockSeparation = 4;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    if (!(x % blockSeparation == 0 && y % blockSeparation == 0 && z % blockSeparation == 0) && chunk.getBlockState(new BlockPos(x, y, z)) != Blocks.BEDROCK.defaultBlockState()) {
                        chunk.setBlockState(new BlockPos(x, y, z), blockState, false);
                    }
                }
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }
}
