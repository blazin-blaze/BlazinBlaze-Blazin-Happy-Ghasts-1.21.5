package net.blazinblaze.happyghastmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.blazinblaze.happyghastmod.block.HappyGhastBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class NeutralDriedGhast extends FacingBlock implements Waterloggable {
    public static final MapCodec<NeutralDriedGhast> CODEC = createCodec(NeutralDriedGhast::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);
    public static final BooleanProperty WATERLOGGED;

    public NeutralDriedGhast(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.UP).with(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(WATERLOGGED, bl);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(WATERLOGGED)) {
            float f = 0.1F;
            world.setBlockState(pos, HappyGhastBlocks.HYDRATED_GHAST.getDefaultState().with(HydratedGhast.WATERLOGGED, true).with(HydratedGhast.FACING, state.get(FACING)));
            if (random.nextFloat() < 0.35F) {
                world.setBlockState(pos, HappyGhastBlocks.HYDRATED_GHAST.getDefaultState().with(HydratedGhast.WATERLOGGED, true).with(HydratedGhast.FACING, state.get(FACING)));
                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0.2, 0, 0.5);
            }
        }else {
            float f = 0.1F;
            world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0.2, 0, 0.5);
            if (random.nextFloat() < 0.35F) {
                world.setBlockState(pos, HappyGhastBlocks.DRIED_GHAST.getDefaultState().with(DriedGhast.WATERLOGGED, false).with(DriedGhast.FACING, state.get(FACING)));
                world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0.2, 0, 0.5);
            }
        }
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
    }

}
