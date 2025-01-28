package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public abstract class BaseDiodeBlock extends DiodeBlock {

    private static final VoxelShape SHAPE_N = Shapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 3.0D, 12.0D, 4.0D, 11.0D), BooleanOp.OR);
    private static final VoxelShape SHAPE_S = Shapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 5.0D, 12.0D, 4.0D, 13.0D), BooleanOp.OR);
    private static final VoxelShape SHAPE_E = Shapes.join(CircuitBaseBlock.BASE, Block.box(5.0D, 2.0D, 4.0D, 13.0D, 4.0D, 12.0D), BooleanOp.OR);
    private static final VoxelShape SHAPE_W = Shapes.join(CircuitBaseBlock.BASE, Block.box(3.0D, 2.0D, 4.0D, 11.0D, 4.0D, 12.0D), BooleanOp.OR);

    public BaseDiodeBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWERED, this.shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    @Override
    protected int getDelay(BlockState state) {
        return 2;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        if (state.getValue(POWERED)) {
            Direction direction = state.getValue(FACING);
            double x = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
            double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            float f = -5.0F / 16.0F;
            double xOffset = (double) (f * (float) direction.getStepX());
            double zOffset = (double) (f * (float) direction.getStepZ());
            level.addParticle(DustParticleOptions.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
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
    public static int getRedstonePowerRelative(Level level, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        int i = level.getSignal(blockpos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState state = level.getBlockState(blockpos);
            return Math.max(i, state.is(Blocks.REDSTONE_WIRE) ? state.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }

}
