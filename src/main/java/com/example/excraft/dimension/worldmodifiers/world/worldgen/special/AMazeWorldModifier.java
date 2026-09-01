package com.example.excraft.dimension.worldmodifiers.world.worldgen.special;

import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AMazeWorldModifier extends WorldWorldModifierType implements SpecialWorldGenModifier, WorldGenWorldModifier {
    private static final String modifierName = "Maze World";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = 0;
    private static boolean active = false;
    private static String type = "specialchunkgenerator";
  //  private AMazeWorldGenerator chunkPlanner;

    public AMazeWorldModifier() {
     //   this.chunkPlanner = new AMazeWorldGenerator();
    }

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

    public void activateEffect() {
     //   chunkPlanner = new AMazeWorldGenerator();
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
        if (chunk.getPos().z == -1 && chunk.getPos().x == 0
                || chunk.getPos().z == 0 && chunk.getPos().x == -1
                || chunk.getPos().z == -1 && chunk.getPos().x == -1
                || chunk.getPos().z == 0 && chunk.getPos().x == 0) {
            return;
        }
        StringBuilder string = new StringBuilder(String.valueOf(chunk.getPos().x));
        string.append(chunk.getPos().z);
        string.append(ExcraftDimensionManager.getCurrentManager().getSalt());
        RandomSource randomSource = RandomSource.create(string.hashCode());
        boolean hole = randomSource.nextIntBetweenInclusive(0,100) < 90;
        boolean wallDirection = randomSource.nextBoolean();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    if (wallDirection) {
                        northWall(chunk,x,y,z,hole);
                    } else {
                        westWall(chunk,x,y,z,hole);
                    }
                }
            }
        }
    }

    private void northWall(ChunkAccess chunk,int x,int y,int z, boolean wallHole) {
        if (x > 8 && wallHole) {
            chunk.setBlockState(new BlockPos(x, y, z), ExcraftBlocks.UNBREAKABLE_SANDSTONE.get().defaultBlockState(),false);
        }/* else {
            if (x> 8 && z < 4 || x > 8 && z > 12) {
              //  chunk.setBlockState(new BlockPos(x, y, z), ExcraftBlocks.UNBREAKABLE_SANDSTONE.get().defaultBlockState(), false);
            }
        }*/
    }

    private void westWall(ChunkAccess chunk,int x,int y,int z,  boolean wallHole ) {
        if (z > 8 && wallHole) {
            chunk.setBlockState(new BlockPos(x, y, z), ExcraftBlocks.UNBREAKABLE_SANDSTONE.get().defaultBlockState(),false);
        } /*else {
            if (z > 8 && x < 4 || z > 8 && x > 12) {
                //  chunk.setBlockState(new BlockPos(x, y, z), ExcraftBlocks.UNBREAKABLE_SANDSTONE.get().defaultBlockState(), false);
            }
        }*/
    }

    @Override
    public String getType() {
        return type;
    }
}
