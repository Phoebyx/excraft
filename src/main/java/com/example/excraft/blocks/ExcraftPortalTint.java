package com.example.excraft.blocks;

import com.example.excraft.dimension.ExcraftDimensionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

import static java.lang.Integer.parseInt;

public class ExcraftPortalTint {
    // on the mod event bus only on the physical client
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            return getColorFromListByState(state);
            },
                ExcraftBlocks.EXCRAFT_PORTAL.value());
    }

    public static List<Integer> getCurrentColour() {
        List<Integer> colours;
        colours = List.of(
                0xa6c91717, //Red
                0xa6fc8803, //Orange
                0xa6fce303, //Yellow
                0xa688fc03, //Light Green
                0xa648fa12  //Green
        );
        return colours;
    }

    private static int getColorFromListByState(BlockState state) {
        int getAge = state.getValue(ExcraftPortalBlock.COLOR);
        return getCurrentColour().get(getAge);
    }

    public static void updatePortalColorOnTickSchedule(ServerTickEvent event) {
        Iterable<BlockPos> blockPos = BlockPos.betweenClosed(-10,-64,-10,10,384,10);
        for (BlockPos pos : blockPos) {
            event.getServer().overworld().scheduleTick(pos,ExcraftBlocks.EXCRAFT_PORTAL.get(),5);
            event.getServer().getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL).scheduleTick(pos,ExcraftBlocks.EXCRAFT_PORTAL.get(),5);
        }
    }


}
