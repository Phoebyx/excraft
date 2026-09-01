package com.example.excraft.dimension;

import com.example.excraft.Config;
import com.example.excraft.blocks.ExcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class SolidWorldBorderCreator {
    private static int radius;

    public SolidWorldBorderCreator() {
        radius = Config.BORDERRADIUS.get();
    }

    public static int getRadius() {
        return radius;
    }

    public boolean isOutsideBorder(ChunkAccess chunk) {
        int x = Math.max(chunk.getPos().getMinBlockX(),chunk.getPos().getMaxBlockX());
        int z = Math.max(chunk.getPos().getMinBlockZ(),chunk.getPos().getMaxBlockZ());
        int addRadius = radius + 23;
        long circle = (long) x *x + (long) z *z - (long) addRadius *addRadius;
        if (circle >= 0) {
            return true;
        }
        return false;
    }

    public boolean isInBorder(ChunkAccess chunk) {
        int x = Math.min(chunk.getPos().getMinBlockX(),chunk.getPos().getMaxBlockX());
        int z = Math.min(chunk.getPos().getMinBlockZ(),chunk.getPos().getMaxBlockZ());
        int addRadius = radius - 23;
        long circle = (long) x *x + (long) z *z - (long) addRadius *addRadius;
        if (circle >= 0) {
            return true;
        }
        return false;
    }

    private boolean isOutsideBorderBlock(int x,int z) {
        long circle = (long) x*x + (long) z *z - (long) radius * radius;
        if (circle >= 0) {
            return true;
        }
        return false;
    }

    public void fillWithBlock(ChunkAccess chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    placeWallBlock(chunk, x, y, z);
                }
            }
        }
    }

    public static void placeWallBlock(ChunkAccess chunk, int x, int y, int z) {
        BlockState blockState;
        if (x % 2 == 0 && y % 2 == 0 && z % 2 == 0) {
            blockState = ExcraftBlocks.UNBREAKABLE_CHISELED_SANDSTONE.get().defaultBlockState();
        } else {blockState = ExcraftBlocks.UNBREAKABLE_SANDSTONE.get().defaultBlockState();}
        chunk.setBlockState(new BlockPos(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z), blockState, false);
    }

    public void fillWithBorder(ChunkAccess chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (isOutsideBorderBlock(x + chunk.getPos().getMinBlockX(),z + chunk.getPos().getMinBlockZ())) {
                    for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                        placeWallBlock(chunk, x, y, z);
                    }
                }
            }
        }
    }
}
