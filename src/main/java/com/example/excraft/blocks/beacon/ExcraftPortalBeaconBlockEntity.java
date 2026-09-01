package com.example.excraft.blocks.beacon;

import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;

public class ExcraftPortalBeaconBlockEntity extends BlockEntity {
    public ExcraftPortalBeaconBlockEntity(BlockPos pos, BlockState blockState) {
        super(ExcraftBlocks.PORTAL_BEACON_ENTITY.get(), pos, blockState);
    }

    @Override
    public void onChunkUnloaded() {
        Excraft.LOGGER.info("CHUNK UNLOADED");
        super.onChunkUnloaded();
    }
}
