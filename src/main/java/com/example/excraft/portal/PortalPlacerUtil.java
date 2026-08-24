package com.example.excraft.portal;

import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public class PortalPlacerUtil {
    private Level level;
    private final int upperValid = 10;
    private final int lowerValid = 15;
    int startingPoint;
    int maxHeight;
    int minHeight;

    public PortalPlacerUtil(Level level) {
        this.level = level;
        startingPoint = level.getHeight();
        maxHeight = level.getMaxBuildHeight();
        minHeight = level.getMinBuildHeight();
    }
    public int findBlockFromTop(Block block) {
        int portalFoundAtY = minHeight - 1;
        for (int i = maxHeight; i >= minHeight; i--) {
            Block currentBlock = level.getBlockState(new BlockPos(0,i,0)).getBlock();
            if (currentBlock == block) {
                portalFoundAtY = i;
                return portalFoundAtY;
            }
        }
        return portalFoundAtY;
    }
    public int findBlockFromBottom(Block block) {
        int portalFoundAtY = maxHeight + 1;
        for (int i = minHeight; i <= maxHeight; i++) {
            BlockState currentBlock = level.getBlockState(new BlockPos(0,i,0));
            if (currentBlock.is(block)) {
                portalFoundAtY = i;
                Excraft.LOGGER.info(String.valueOf(i));
                return portalFoundAtY;
            }
        }
        return portalFoundAtY;
    }
}