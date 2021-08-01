package com.tristankechlo.additionalredstone.blocks;

import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import com.tristankechlo.additionalredstone.util.ToggleLatchSide;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

public class ToggleLatchBlock extends HorizontalBlock {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	public static final EnumProperty<ToggleLatchSide> POWERED_SIDE = EnumProperty.create("outputside",
			ToggleLatchSide.class);

	public ToggleLatchBlock() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.getDefaultDiodeState());
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canSupportRigidBlock(worldIn, pos.below());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		boolean inputPowered = this.calculateInputStrength(worldIn, pos, state) > 0;
		if (inputPowered) {
			worldIn.setBlock(pos, state.cycle(POWERED_SIDE), 2);
			this.notifyNeighbors(worldIn, pos, state);
		}
	}

	private void updateState(World worldIn, BlockPos pos, BlockState state) {
		if (!worldIn.getBlockTicks().willTickThisTick(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			worldIn.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
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
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return side != state.getValue(FACING);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!player.abilities.mayBuild) {
			return ActionResultType.PASS;
		} else {
			worldIn.setBlock(pos, state.cycle(POWERED_SIDE), 3);
			this.playSound(player, worldIn, pos, true);
			return ActionResultType.sidedSuccess(worldIn.isClientSide);
		}
	}

	private void playSound(@Nullable PlayerEntity playerIn, IWorld worldIn, BlockPos pos, boolean hitByArrow) {
		worldIn.playSound(playerIn, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	@Override
	public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return this.getSignal(blockState, blockAccess, pos, side);
	}

	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
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
		return this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED_SIDE,
				ToggleLatchSide.LEFT);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		Direction direction = ctx.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED_SIDE, ToggleLatchSide.LEFT);
	}

	private void notifyNeighbors(World worldIn, BlockPos pos, BlockState state) {
		ToggleLatchSide side = state.getValue(POWERED_SIDE);
		if (side == ToggleLatchSide.LEFT) {
			Direction direction = state.getValue(FACING).getClockWise();
			BlockPos blockpos = pos.relative(direction);
			if (ForgeEventFactory
					.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false)
					.isCanceled()) {
				return;
			}
			worldIn.neighborChanged(blockpos, this, pos);
			worldIn.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
		} else if (side == ToggleLatchSide.RIGHT) {
			Direction direction = state.getValue(FACING).getCounterClockWise();
			BlockPos blockpos = pos.relative(direction);
			if (ForgeEventFactory
					.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false)
					.isCanceled()) {
				return;
			}
			worldIn.neighborChanged(blockpos, this, pos);
			worldIn.updateNeighborsAtExceptFromFacing(blockpos, this, direction.getOpposite());
		}
	}

	private int getDelay(BlockState state) {
		return 0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		ToggleLatchSide side = stateIn.getValue(POWERED_SIDE);
		if (side == ToggleLatchSide.LEFT) {
			this.spawnParticle(stateIn, worldIn, pos, rand, true);
		} else if (side == ToggleLatchSide.RIGHT) {
			this.spawnParticle(stateIn, worldIn, pos, rand, false);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void spawnParticle(BlockState state, World world, BlockPos pos, Random rand, boolean left) {
		double xOff = left ? 0.25D : 0.75D;
		Direction direction = state.getValue(FACING);
		double x = (double) pos.getX() + xOff + (rand.nextDouble() - 0.5D) * 0.2D;
		double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
		double z = (double) pos.getZ() + 0.65D + (rand.nextDouble() - 0.5D) * 0.2D;
		float f = -5.0F / 16.0F;
		double xOffset = (double) (f * (float) direction.getStepX());
		double zOffset = (double) (f * (float) direction.getStepZ());
		world.addParticle(RedstoneParticleData.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
	}

	private int calculateInputStrength(World worldIn, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction);
		int i = worldIn.getSignal(blockpos, direction);
		if (i >= 15) {
			return i;
		} else {
			BlockState blockstate = worldIn.getBlockState(blockpos);
			return Math.max(i, blockstate.is(Blocks.REDSTONE_WIRE) ? blockstate.getValue(RedstoneWireBlock.POWER) : 0);
		}
	}

}
