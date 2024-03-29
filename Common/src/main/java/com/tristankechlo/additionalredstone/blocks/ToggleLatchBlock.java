package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class ToggleLatchBlock extends HorizontalDirectionalBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final EnumProperty<ToggleLatchSide> POWERED_SIDE = EnumProperty.create("outputside", ToggleLatchSide.class);

    public ToggleLatchBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.getDefaultDiodeState());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return canSupportRigidBlock(worldIn, pos.below());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        boolean inputPowered = this.calculateInputStrength(worldIn, pos, state) > 0;
        if (inputPowered) {
            worldIn.setBlock(pos, state.cycle(POWERED_SIDE), 2);
            this.notifyNeighbors(worldIn, pos, state);
        }
    }

    private void updateState(Level worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            worldIn.scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        // only allow blockupdates to have an effect when they are on the input side
        Direction inputDirection = state.getValue(FACING);
        BlockPos inputPos = pos.relative(inputDirection);
        if (!inputPos.equals(fromPos)) {
            return;
        }
        if (state.canSurvive(worldIn, pos)) {
            this.updateState(worldIn, pos, state);
        } else {
            dropResources(state, worldIn, pos, null);
            worldIn.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                worldIn.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            worldIn.setBlock(pos, state.cycle(POWERED_SIDE), 3);
            this.playSound(player, worldIn, pos, true);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
    }

    private void playSound(Player playerIn, LevelAccessor worldIn, BlockPos pos, boolean hitByArrow) {
        worldIn.playSound(playerIn, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return this.getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        Direction right = blockState.getValue(FACING).getClockWise();
        Direction left = blockState.getValue(FACING).getCounterClockWise();
        if (side == left && blockState.getValue(POWERED_SIDE) == ToggleLatchSide.LEFT) {
            return 15;
        } else if (side == right && blockState.getValue(POWERED_SIDE) == ToggleLatchSide.RIGHT) {
            return 15;
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED_SIDE);
    }

    private BlockState getDefaultDiodeState() {
        return this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED_SIDE, ToggleLatchSide.LEFT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED_SIDE, ToggleLatchSide.LEFT);
    }

    private void notifyNeighbors(Level worldIn, BlockPos pos, BlockState state) {
        ToggleLatchSide side = state.getValue(POWERED_SIDE);
        if (side == ToggleLatchSide.LEFT) {
            Direction direction = state.getValue(FACING).getClockWise();
            BlockPos blockpos = pos.relative(direction);
            /*TODO post forge/fabric event
            if (ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false).isCanceled()) {
                return;
            }*/
            worldIn.neighborChanged(blockpos, this, pos);
            worldIn.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
        } else if (side == ToggleLatchSide.RIGHT) {
            Direction direction = state.getValue(FACING).getCounterClockWise();
            BlockPos blockpos = pos.relative(direction);
            /*TODO post forge/fabric event
            if (ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false).isCanceled()) {
                return;
            }*/
            worldIn.neighborChanged(blockpos, this, pos);
            worldIn.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
        }
    }

    private int getDelay(BlockState state) {
        return 0;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        boolean leftSide = stateIn.getValue(POWERED_SIDE) == ToggleLatchSide.LEFT;
        this.spawnParticle(stateIn, worldIn, pos, rand, leftSide);
    }

    public void spawnParticle(BlockState state, Level world, BlockPos pos, RandomSource rand, boolean left) {
        double offset = left ? -0.25D : 0.25D;
        Direction direction = state.getValue(FACING);

        // center of the block
        double x = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
        double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
        double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;

        // offset to the side its facing
        float f = -3.0F / 16.0F;
        double xOffset = f * (float) direction.getStepX();
        double zOffset = f * (float) direction.getStepZ();

        // offset to left or right side
        xOffset += offset * (double) direction.getStepZ();
        zOffset -= offset * (double) direction.getStepX();

        world.addParticle(DustParticleOptions.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
    }

    private int calculateInputStrength(Level worldIn, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction);
        int i = worldIn.getSignal(blockpos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, blockstate.is(Blocks.REDSTONE_WIRE) ? blockstate.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }

    private enum ToggleLatchSide implements StringRepresentable {

        LEFT("left"),
        RIGHT("right");

        private final String name;

        ToggleLatchSide(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

    }
}
