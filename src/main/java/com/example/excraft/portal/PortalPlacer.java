package com.example.excraft.portal;

import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PortalPlacer {
    public static void createPortal(Level level) {
        PortalPlacerUtil blockUtil = new PortalPlacerUtil(level);
        int lowestBedrockFoundAtY = blockUtil.findBlockFromBottom(Blocks.BEDROCK);
        int highestBedrockFoundAtY = blockUtil.findBlockFromTop(Blocks.BEDROCK);
        int distanceOfBedrocks = highestBedrockFoundAtY - lowestBedrockFoundAtY;
        if (distanceOfBedrocks >= 30) {
            PlaceOriginPortal.placeExitPortal(level.getServer(),level.getMinBuildHeight() + distanceOfBedrocks/2);
        } else if (distanceOfBedrocks < 8) {
            Excraft.LOGGER.info(String.valueOf(level.getLevelData().getSpawnPos().getY()));
            PlaceOriginPortal.placeExitPortal(level.getServer(),level.getLevelData().getSpawnPos().getY());
        }
        else PlaceOriginPortal.placeExitPortal(level.getServer(),(level.getMaxBuildHeight() + level.getMinBuildHeight())/2); {
        }
    }
/*
    private BlockPos portalDestination(Level level) {
        PortalPlacerUtil blockUtil = new PortalPlacerUtil(level);
        int portalFoundAtY = blockUtil.findBlockFromBottom(ExcraftBlocks.EXCRAFT_PORTAL.get());
        if (portalFoundAtY != Integer.MAX_VALUE) {
            return new BlockPos(0,portalFoundAtY,0);
        } else {
            return createPortalAndReturnLocation(level);
        }
    }*/
}
