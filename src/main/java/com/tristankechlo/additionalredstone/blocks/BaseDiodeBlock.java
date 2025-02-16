package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BaseDiodeBlock extends RedstoneDiodeBlock {

    private static final VoxelShape SHAPE_N = VoxelShapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 3.0D, 12.0D, 4.0D, 11.0D), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_S = VoxelShapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 5.0D, 12.0D, 4.0D, 13.0D), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_E = VoxelShapes.join(CircuitBaseBlock.BASE, Block.box(5.0D, 2.0D, 4.0D, 13.0D, 4.0D, 12.0D), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_W = VoxelShapes.join(CircuitBaseBlock.BASE, Block.box(3.0D, 2.0D, 4.0D, 11.0D, 4.0D, 12.0D), IBooleanFunction.OR);

    public BaseDiodeBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.NORTH) {
            return SHAPE_N;
        } else if (facing == Direction.EAST) {
            return SHAPE_E;
        } else if (facing == Direction.SOUTH) {
            return SHAPE_S;
        }
        return SHAPE_W;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWERED, this.shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    @Override
    protected int getDelay(BlockState state) {
        return 2;
    }

    @Override
    public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
        if (state.getValue(POWERED)) {
            Direction direction = state.getValue(FACING);
            double x = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
            double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            float f = -5.0F / 16.0F;
            double xOffset = (double) (f * (float) direction.getStepX());
            double zOffset = (double) (f * (float) direction.getStepZ());
            level.addParticle(RedstoneParticleData.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    /**
     * check the redstone signal strength at a position relative to a given position
     *
     * @param level     level
     * @param pos       the position of the logic-block
     * @param direction the direction to check in relative from 'pos'
     * @return the redstone signal strength
     */
    public static int getRedstonePowerRelative(World level, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        int i = level.getSignal(blockpos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState state = level.getBlockState(blockpos);
            return Math.max(i, state.is(Blocks.REDSTONE_WIRE) ? state.getValue(RedstoneWireBlock.POWER) : 0);
        }
    }

}
