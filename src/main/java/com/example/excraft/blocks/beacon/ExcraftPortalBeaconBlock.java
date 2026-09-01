package com.example.excraft.blocks.beacon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExcraftPortalBeaconBlock extends Block implements EntityBlock {
    public ExcraftPortalBeaconBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExcraftPortalBeaconBlockEntity(pos,state);
    }
}
