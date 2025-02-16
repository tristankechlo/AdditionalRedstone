package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.tileentity.ToggleLatchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ToggleLatchBlock extends HorizontalBlock {

    private static final int DELAY = 0;
    private static final EnumProperty<ToggleLatchSide> POWERED_SIDE = EnumProperty.create("outputside", ToggleLatchSide.class);

    public ToggleLatchBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED_SIDE, ToggleLatchSide.LEFT));
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return CircuitBaseBlock.BASE;
    }

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
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

    private void updateNeighbours(World level, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        level.neighborChanged(blockpos, this, pos);
        level.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
    }

    private void checkTickOnNeighbor(World level, BlockPos pos, BlockState state) {
        TileEntity blockEntity = level.getBlockEntity(pos);
        boolean change = false;
        if (blockEntity instanceof ToggleLatchTileEntity) {
            Direction direction = state.getValue(FACING);
            boolean input = BaseDiodeBlock.getRedstonePowerRelative(level, pos, direction) > 0;
            change = ((ToggleLatchTileEntity) blockEntity).shouldBePowered(input);
        }
        if (change && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.getBlockTicks().scheduleTick(pos, this, DELAY, tickpriority);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        // only allow blockupdates to have an effect when they are on the input side
        Direction inputDirection = state.getValue(FACING);
        BlockPos inputPos = pos.relative(inputDirection);
        if (!inputPos.equals(fromPos)) {
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
    private boolean shouldPrioritize(World level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState neighbour = level.getBlockState(pos.relative(direction));
        return neighbour.hasProperty(FACING) && neighbour.getValue(FACING) != direction;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return side != state.getValue(FACING);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.abilities.mayBuild) {
            return ActionResultType.PASS;
        } else {
            level.setBlock(pos, state.cycle(POWERED_SIDE), 3);
            this.playSound(player, level, pos);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
    }

    private void playSound(PlayerEntity player, IWorld level, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    public int getDirectSignal(BlockState state, IBlockReader level, BlockPos pos, Direction side) {
        return this.getSignal(state, level, pos, side);
    }

    @Override
    public int getSignal(BlockState state, IBlockReader level, BlockPos pos, Direction side) {
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
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED_SIDE, ToggleLatchSide.LEFT);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ToggleLatchTileEntity();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World level, BlockPos pos, Random rand) {
        boolean leftSide = state.getValue(POWERED_SIDE) == ToggleLatchSide.LEFT;
        this.spawnParticle(state, level, pos, rand, leftSide);
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticle(BlockState state, World level, BlockPos pos, Random rand, boolean left) {
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

        level.addParticle(RedstoneParticleData.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
    }

    public enum ToggleLatchSide implements IStringSerializable {

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
