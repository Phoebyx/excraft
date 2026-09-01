package com.example.excraft.blocks.portal;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.portal.PlaceOriginPortal;
import com.example.excraft.portal.PortalPlacerUtil;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ExcraftPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final IntegerProperty COLOR = BlockStateProperties.AGE_5;
    public static final int MAX_AGE = 5;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Excraft.MODID);
    private static final Supplier<AttachmentType<Integer>> LASTTPTIME = ATTACHMENT_TYPES.register(
            "lasttptime", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "excraft");
    public static final Holder<SoundEvent> PORTAL_SOUND = SOUND_EVENTS.register("portalsound", SoundEvent::createVariableRangeEvent);
    private static boolean areChunksForced = false;

    public ExcraftPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), Integer.valueOf(0)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        builder.add(COLOR);
    }
    protected IntegerProperty getAgeProperty() {
        return COLOR;
    }

    public BlockState getStateForColor(int color) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(color));
    }

    public BlockState updateStateForColor(int color, BlockState state) {
        return state.setValue(this.getAgeProperty(), Integer.valueOf(color));
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(AXIS) == Direction.Axis.Z) {
            return Z_AXIS_AABB;
        } else {
            return X_AXIS_AABB;
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int currentTimerColour = ExcraftTimer.intPortalBlockColor(level);
        if (this.getAge(state) != currentTimerColour); {
           level.setBlock(pos, this.updateStateForColor(currentTimerColour,state),2);
        }
        if (!areChunksForced) {
            Excraft.LOGGER.info("ChunkForced");
            ChunkPos chunkPos = new ChunkPos(pos);
            level.getChunkSource().updateChunkForced(chunkPos,true);
            areChunksForced = true;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        Direction.Axis direction$axis = facing.getAxis();
        Direction.Axis direction$axis1 = state.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.getBlockState(neighborPos).is(Blocks.WATER)){
            level.destroyBlock(neighborPos,false);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (isEntityInCooldown(entity,level) || entity instanceof ItemEntity) {
            return;
        }
        try {
            ServerLevel serverlevel;
            BlockPos destinationBlock = new BlockPos(0,0,0);
            if (entity.level().dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL)
                serverlevel = level.getServer().getLevel(Level.OVERWORLD);
            else {
                serverlevel = level.getServer().getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL);
            }
            destinationBlock = portalDestination(serverlevel);
            Excraft.LOGGER.info(String.valueOf(destinationBlock.getY()));
            entity.setData(LASTTPTIME,(int) level.getGameTime());
            entity.teleportTo(serverlevel, entity.getX() + 2, destinationBlock.getY() + 1, entity.getZ(), null, entity.getYRot(), entity.getXRot());
            entity.invulnerableTime = 180;
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
            if (b > level.getMaxBuildHeight() - 10) {b = 0;}
            setPortalAtY = Math.max(a,b);
            Excraft.LOGGER.info("Other/Sky " + setPortalAtY);
        }
        PlaceOriginPortal.placeExitPortal(level.getServer(),setPortalAtY);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(1000) == 0) {
            level.playLocalSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    PORTAL_SOUND.value(),
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        for (int i = 0; i < 4; i++) {
            double d0 = (double)pos.getX() + random.nextDouble();
            double d1 = (double)pos.getY() + random.nextDouble();
            double d2 = (double)pos.getZ() + random.nextDouble();
            double d3 = ((double)random.nextFloat() - 0.5) * 0.5;
            double d4 = ((double)random.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)random.nextFloat() - 0.5) * 0.5;
            int j = random.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                d0 = (double)pos.getX() + 0.5 + 0.25 * (double)j;
                d3 = (double)(random.nextFloat() * 2.0F * (float)j);
            } else {
                d2 = (double)pos.getZ() + 0.5 + 0.25 * (double)j;
                d5 = (double)(random.nextFloat() * 2.0F * (float)j);
            }

            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }
    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)state.getValue(AXIS)) {
                    case Z -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.X);
                    }
                    case X -> {
                        return (BlockState)state.setValue(AXIS, Direction.Axis.Z);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

}
