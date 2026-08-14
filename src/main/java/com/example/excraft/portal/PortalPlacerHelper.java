package com.example.excraft.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public class PortalPlacerHelper {

    private MinecraftServer server;
    private ServerLevel levelToPlace;
    private final int upperValid = 10;
    private final int lowerValid = 15;
    int startingPoint;
    int maxHeight;
    int minHeight;

    public PortalPlacerHelper(MinecraftServer server, ServerLevel levelToPlace) {
        this.server = server;
        this.levelToPlace = levelToPlace;
        startingPoint = levelToPlace.getHeight();
        maxHeight = levelToPlace.getMaxBuildHeight();
        minHeight = levelToPlace.getMinBuildHeight();
    }

    public int getValidHeightToPlacePortal() {
        return 1;
    }
    private int getHighestBlock(int i) {
        levelToPlace.getBlockState(new BlockPos(0,i,0));
        return 1;
    }

    private boolean isPlacedOnVoid() {
    return startingPoint <= minHeight + lowerValid;
    }

    private boolean dimensionHasRoof() {
        Iterable<BlockPos> blocksToValidate = BlockPos.betweenClosed(0,startingPoint + 1, 0, 0, startingPoint - upperValid,0);
        boolean hasRoof = false;
        for (BlockPos blockPos: blocksToValidate) {
            BlockState currentBlock = levelToPlace.getBlockState(blockPos);
            if(currentBlock.getBlock() == (Blocks.BEDROCK)) {
                hasRoof = true;
            }

        }
        return hasRoof;

    }
}
