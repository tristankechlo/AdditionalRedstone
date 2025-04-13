package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class LedBlock extends HorizontalDirectionalBlock {

    private static final int DELAY = 2;
    private static final IntegerProperty POWER = BlockStateProperties.POWER;
    private static final VoxelShape SHAPE = Shapes.join(CircuitBaseBlock.BASE, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 6.0D, 12.0D), BooleanOp.OR);

    public LedBlock() {
        super(Properties.copy(Blocks.REPEATER).lightLevel(LedBlock::getLightLevel));
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(FACING).getOpposite() == direction ? state.getValue(POWER) - 1 : 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean isMoving) {
        if (state.canSurvive(level, pos)) {
            this.checkTickOnNeighbor(level, pos, state);
        } else {
            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            dropResources(state, level, pos, blockEntity);
            level.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    /* schedule a tick for a state change, when needed */
    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        int power = state.getValue(POWER);
        int input = this.getInputSignal(level, pos, state);
        if (power != input && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority priority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                priority = TickPriority.EXTREMELY_HIGH;
            } else if (power > 0) {
                priority = TickPriority.VERY_HIGH;
            }
            level.scheduleTick(pos, this, DELAY, priority);
        }
    }

    /* when the state change is from the input side, the tick should get priority */
    private boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState neighbour = level.getBlockState(pos.relative(direction));
        return neighbour.hasProperty(FACING) && neighbour.getValue(FACING) != direction;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int power = state.getValue(POWER);
        int input = this.getInputSignal(level, pos, state);
        if (power > 0 && input == 0) {
            level.setBlock(pos, state.setValue(POWER, 0), 2);
        } else if (power != input && input > 0) {
            level.setBlock(pos, state.setValue(POWER, input - 1), 2);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (this.getInputSignal(level, pos, state) > 0) { // check if led should be active
            level.scheduleTick(pos, this, 1); // schedule tick, so the led can turn on
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean $$4) {
        this.updateNeighborsInFront(level, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean $$4) {
        if (!$$4 && !state.is(state2.getBlock())) {
            super.onRemove(state, level, pos, state2, $$4);
            this.updateNeighborsInFront(level, pos, state);
        }
    }

    protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos neighbour = pos.relative(direction);
        level.neighborChanged(neighbour, this, pos);
        level.updateNeighborsAtExceptFromFacing(neighbour, this, direction);
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockPos neighbour = pos.relative(direction);
        int signal = level.getSignal(neighbour, direction);
        if (signal >= 15) {
            return 15;
        } else {
            BlockState stateNeighbour = level.getBlockState(neighbour);
            return Math.max(signal, stateNeighbour.is(Blocks.REDSTONE_WIRE) ? stateNeighbour.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }

}
