package com.tristankechlo.additionalredstone.blocks;

import com.mojang.serialization.MapCodec;
import com.tristankechlo.additionalredstone.blockentity.ToggleLatchBlockEntity;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class ToggleLatchBlock extends HorizontalDirectionalBlock implements EntityBlock {

    private static final int DELAY = 0;
    private static final EnumProperty<ToggleLatchSide> POWERED_SIDE = EnumProperty.create("outputside", ToggleLatchSide.class);
    public static final MapCodec<ToggleLatchBlock> CODEC = MapCodec.unit(ToggleLatchBlock::new);

    public ToggleLatchBlock() {
        super(Properties.ofFullCopy(Blocks.REPEATER));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED_SIDE, ToggleLatchSide.LEFT));
    }

    @Override
    protected MapCodec<ToggleLatchBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CircuitBaseBlock.BASE;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        Direction direction = state.getValue(FACING);
        boolean inputPowered = BaseDiodeBlock.getRedstonePowerRelative(level, pos, direction) > 0;
        if (inputPowered) {
            level.setBlock(pos, state.cycle(POWERED_SIDE), 2); // update this block

            // send block updates for neighbours
            Direction right = state.getValue(FACING).getClockWise();
            Direction left = state.getValue(FACING).getCounterClockWise();
            this.updateNeighbours(level, pos, right);
            this.updateNeighbours(level, pos, left);
        }
    }

    private void updateNeighbours(Level level, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        level.neighborChanged(blockpos, this, pos);
        level.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
    }

    private void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        boolean change = false;
        if (blockEntity instanceof ToggleLatchBlockEntity) {
            Direction direction = state.getValue(FACING);
            boolean input = BaseDiodeBlock.getRedstonePowerRelative(level, pos, direction) > 0;
            change = ((ToggleLatchBlockEntity) blockEntity).shouldBePowered(input);
        }
        if (change && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.scheduleTick(pos, this, DELAY, tickpriority);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean isMoving) {
        // only allow blockupdates to have an effect when they are on the input side
        Direction inputDirection = state.getValue(FACING);
        BlockPos inputPos = pos.relative(inputDirection);
        if (!inputPos.equals(pos2)) {
            return;
        }
        if (state.canSurvive(level, pos)) {
            this.checkTickOnNeighbor(level, pos, state);
        } else {
            dropResources(state, level, pos, null);
            level.removeBlock(pos, false);

            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    /* when the state change is from the input side, the tick should get priority */
    private boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState neighbour = level.getBlockState(pos.relative(direction));
        return neighbour.hasProperty(FACING) && neighbour.getValue(FACING) != direction;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            level.setBlock(pos, state.cycle(POWERED_SIDE), 3);
            this.playSound(player, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private void playSound(Player player, LevelAccessor level, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return this.getSignal(state, level, pos, side);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        Direction right = state.getValue(FACING).getClockWise();
        Direction left = state.getValue(FACING).getCounterClockWise();
        if (side == left && state.getValue(POWERED_SIDE) == ToggleLatchSide.LEFT) {
            return 15;
        } else if (side == right && state.getValue(POWERED_SIDE) == ToggleLatchSide.RIGHT) {
            return 15;
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED_SIDE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED_SIDE, ToggleLatchSide.LEFT);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToggleLatchBlockEntity(pos, state);
    }

    @Override // client only
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        boolean leftSide = stateIn.getValue(POWERED_SIDE) == ToggleLatchSide.LEFT;
        this.spawnParticle(stateIn, level, pos, rand, leftSide);
    }

    public void spawnParticle(BlockState state, Level level, BlockPos pos, RandomSource rand, boolean left) {
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

        level.addParticle(DustParticleOptions.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
    }

    public enum ToggleLatchSide implements StringRepresentable {

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
