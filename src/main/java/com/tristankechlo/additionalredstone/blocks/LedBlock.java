package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class LedBlock extends HorizontalBlock {

    private static final int DELAY = 2;
    private static final IntegerProperty POWER = BlockStateProperties.POWER;
    private static final VoxelShape SHAPE = VoxelShapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 6.0D, 12.0D), IBooleanFunction.OR);

    public LedBlock() {
        super(Properties.copy(Blocks.REPEATER).lightLevel(LedBlock::getLightLevel));
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWER, FACING);
    }

    private static int getLightLevel(BlockState state) {
        if (state.hasProperty(POWER)) {
            return state.getValue(POWER);
        }
        return 0;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // check for block sides done in mixins
    }

    @Override
    public int getDirectSignal(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
        return state.getValue(FACING).getOpposite() == direction ? state.getValue(POWER) - 1 : 0;
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos pos2, boolean isMoving) {
        if (state.canSurvive(level, pos)) {
            this.checkTickOnNeighbor(level, pos, state);
        } else {
            TileEntity blockEntity = state.hasTileEntity() ? level.getBlockEntity(pos) : null;
            dropResources(state, level, pos, blockEntity);
            level.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    /* schedule a tick for a state change, when needed */
    protected void checkTickOnNeighbor(World level, BlockPos pos, BlockState state) {
        int power = state.getValue(POWER);
        int input = this.getInputSignal(level, pos, state);
        if (power != input && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority priority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                priority = TickPriority.EXTREMELY_HIGH;
            } else if (power > 0) {
                priority = TickPriority.VERY_HIGH;
            }
            level.getBlockTicks().scheduleTick(pos, this, DELAY, priority);
        }
    }

    /* when the state change is from the input side, the tick should get priority */
    private boolean shouldPrioritize(World level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState neighbour = level.getBlockState(pos.relative(direction));
        return neighbour.hasProperty(FACING) && neighbour.getValue(FACING) != direction;
    }

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
        int power = state.getValue(POWER);
        int input = this.getInputSignal(level, pos, state);
        if (power > 0 && input == 0) {
            level.setBlock(pos, state.setValue(POWER, 0), 2);
        } else if (power != input && input > 0) {
            level.setBlock(pos, state.setValue(POWER, input - 1), 2);
        }
    }

    @Override
    public void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (this.getInputSignal(level, pos, state) > 0) { // check if led should be active
            level.getBlockTicks().scheduleTick(pos, this, 1); // schedule tick, so the led can turn on
        }
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState state2, boolean $$4) {
        this.updateNeighborsInFront(level, pos, state);
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState state2, boolean $$4) {
        if (!$$4 && !state.is(state2.getBlock())) {
            super.onRemove(state, level, pos, state2, $$4);
            this.updateNeighborsInFront(level, pos, state);
        }
    }

    protected void updateNeighborsInFront(World level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos neighbour = pos.relative(direction);
        level.neighborChanged(neighbour, this, pos);
        level.updateNeighborsAtExceptFromFacing(neighbour, this, direction);
    }

    protected int getInputSignal(World level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockPos neighbour = pos.relative(direction);
        int signal = level.getSignal(neighbour, direction);
        if (signal >= 15) {
            return 15;
        } else {
            BlockState stateNeighbour = level.getBlockState(neighbour);
            return Math.max(signal, stateNeighbour.is(Blocks.REDSTONE_WIRE) ? stateNeighbour.getValue(RedstoneWireBlock.POWER) : 0);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader level, BlockPos pos, Direction side) {
        Direction front = state.getValue(FACING);
        Direction back = state.getValue(FACING).getOpposite();
        return side == front || side == back;
    }

}
