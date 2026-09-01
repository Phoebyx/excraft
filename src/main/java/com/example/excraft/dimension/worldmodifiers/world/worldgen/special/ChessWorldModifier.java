package com.example.excraft.dimension.worldmodifiers.world.worldgen.special;

import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.SolidWorldBorderCreator;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChessWorldModifier extends WorldWorldModifierType implements SpecialWorldGenModifier, WorldGenWorldModifier {
    private static final String modifierName = "Chess World";
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
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    if (Math.abs(chunk.getPos().z) % 2 == 1 ? Math.abs(chunk.getPos().x) % 2 == 0 : Math.abs(chunk.getPos().x) % 2 == 1) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }
    }
    /*public void chunkGenerator(ChunkAccess chunk, int x, int y, int z) {;
        if (chunk.getPos().x % 3 != 0 && chunk.getPos().z % 3 != 0) {
            chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(),false);
        }
    }

     */

    @Override
    public String getType() {
        return type;
    }
}
