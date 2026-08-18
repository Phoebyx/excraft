package com.example.excraft.blocks;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.dimension.DimensionManager;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.portal.PlaceOriginPortal;
import com.example.excraft.portal.PortalPlacerUtil;
import com.mojang.serialization.Codec;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Supplier;

public class ExcraftPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Excraft.MODID);
    private static final Supplier<AttachmentType<Integer>> LASTTPTIME = ATTACHMENT_TYPES.register(
            "lasttptime", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );
    public ExcraftPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (isEntityInCooldown(entity,level)) {
            return;
        }
        try {
            ServerLevel serverlevel;
            BlockPos destinationBlock = new BlockPos(0,0,0);
            if (entity.level().dimension() == DimensionManager.EXCRAFT_LEVEL)
                serverlevel = level.getServer().getLevel(Level.OVERWORLD);
            else {
                serverlevel = level.getServer().getLevel(DimensionManager.EXCRAFT_LEVEL);
            }
            destinationBlock = portalDestination(serverlevel);
            Excraft.LOGGER.info(String.valueOf(destinationBlock.getY()));
            entity.setData(LASTTPTIME,(int) level.getGameTime());
            entity.teleportTo(serverlevel, entity.getX() + 2, destinationBlock.getY() + 1, entity.getZ() + 2, null, entity.getYRot(), entity.getXRot());
        } catch (NullPointerException e) {
            return;
        }
    }
    //Returns true if entity teleported in the last @Config.TPCOOLDOWN seconds
    private boolean isEntityInCooldown(Entity entity,Level level) {
        return (level.getGameTime() - entity.getData(LASTTPTIME) < Config.TPCOOLDOWN.get() * 20);
    }

    private BlockPos portalDestination(ServerLevel serverlevel) {
        PortalPlacerUtil blockUtil = new PortalPlacerUtil(serverlevel);
        int portalFoundAtY = blockUtil.findBlockFromBottom(ExcraftBlocks.EXCRAFT_PORTAL.get());
        if (portalFoundAtY > 999999) {
            return new BlockPos(0,portalFoundAtY,0);
        } else {
            return new BlockPos (0,blockUtil.findBlockFromBottom(ExcraftBlocks.EXCRAFT_PORTAL.get()),0);
        }
    }

    public static void createPortalAndReturnLocation(ServerLevel level) {
        PortalPlacerUtil blockUtil = new PortalPlacerUtil(level);
        int lowestBedrockFoundAtY = blockUtil.findBlockFromBottom(Blocks.BEDROCK);
        int highestBedrockFoundAtY = blockUtil.findBlockFromTop(Blocks.BEDROCK);
        int distanceOfBedrocks = highestBedrockFoundAtY - lowestBedrockFoundAtY;
        int setPortalAtY;
        if (distanceOfBedrocks >= 30) {
            setPortalAtY = level.getMinBuildHeight() + distanceOfBedrocks/2;
            Excraft.LOGGER.info("BedrockDistance > 30/Cave " + setPortalAtY);
        } else if (distanceOfBedrocks < 8) {
            setPortalAtY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG,0,0);
            Excraft.LOGGER.info("BedrockDistance < 8/Overworld " + setPortalAtY);
        } else {
            int a = (level.getMaxBuildHeight() + level.getMinBuildHeight())/2;
            int b = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG,0,0);
            setPortalAtY = Math.max(a,b);
            Excraft.LOGGER.info("Other/Sky " + setPortalAtY);
        }
        PlaceOriginPortal.placeExitPortal(level.getServer(),setPortalAtY);
    }
}
