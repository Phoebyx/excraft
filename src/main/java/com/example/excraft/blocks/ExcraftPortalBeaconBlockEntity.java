package com.example.excraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ExcraftPortalBeaconBlockEntity extends BlockEntity {

    public ExcraftPortalBeaconBlockEntity(BlockPos pos, BlockState blockState) {
        super(ExcraftBlocks.PORTAL_BEACON_ENTITY.get(), pos, blockState);
    }

}
