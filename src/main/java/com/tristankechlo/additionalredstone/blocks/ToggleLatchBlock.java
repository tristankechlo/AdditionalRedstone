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

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	public static final EnumProperty<ToggleLatchSide> POWERED_SIDE = EnumProperty.create("outputside",
			ToggleLatchSide.class);

	public ToggleLatchBlock() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.getDefaultDiodeState());
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return hasSolidSideOnTop(worldIn, pos.down());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		boolean inputPowered = this.calculateInputStrength(worldIn, pos, state) > 0;
		if (inputPowered) {
			worldIn.setBlockState(pos, state.cycleValue(POWERED_SIDE), 2);
			this.notifyNeighbors(worldIn, pos, state);
		}
	}

	private void updateState(World worldIn, BlockPos pos, BlockState state) {
		if (!worldIn.getPendingBlockTicks().isTickPending(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		// only allow blockupdates to have an effect when they are on the input side
		Direction inputDirection = state.get(HORIZONTAL_FACING);
		BlockPos inputPos = pos.offset(inputDirection);
		if (!inputPos.equals(fromPos)) {
			return;
		}
		if (state.isValidPosition(worldIn, pos)) {
			this.updateState(worldIn, pos, state);
		} else {
			spawnDrops(state, worldIn, pos, null);
			worldIn.removeBlock(pos, false);
			for (Direction direction : Direction.values()) {
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}
		}
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return side != state.get(HORIZONTAL_FACING);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (!player.abilities.allowEdit) {
			return ActionResultType.PASS;
		} else {
			worldIn.setBlockState(pos, state.cycleValue(POWERED_SIDE), 3);
			this.playSound(player, worldIn, pos, true);
			return ActionResultType.func_233537_a_(worldIn.isRemote);
		}
	}

	private void playSound(@Nullable PlayerEntity playerIn, IWorld worldIn, BlockPos pos, boolean hitByArrow) {
		worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return this.getWeakPower(blockState, blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		Direction right = blockState.get(HORIZONTAL_FACING).rotateY();
		Direction left = blockState.get(HORIZONTAL_FACING).rotateYCCW();
		if (side == left && blockState.get(POWERED_SIDE) == ToggleLatchSide.LEFT) {
			return 15;
		} else if (side == right && blockState.get(POWERED_SIDE) == ToggleLatchSide.RIGHT) {
			return 15;
		}
		return 0;
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED_SIDE);
	}

	private BlockState getDefaultDiodeState() {
		return this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED_SIDE,
				ToggleLatchSide.LEFT);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		Direction direction = ctx.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(POWERED_SIDE, ToggleLatchSide.LEFT);
	}

	private void notifyNeighbors(World worldIn, BlockPos pos, BlockState state) {
		ToggleLatchSide side = state.get(POWERED_SIDE);
		if (side == ToggleLatchSide.LEFT) {
			Direction direction = state.get(HORIZONTAL_FACING).rotateY();
			BlockPos blockpos = pos.offset(direction);
			if (ForgeEventFactory
					.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false)
					.isCanceled()) {
				return;
			}
			worldIn.neighborChanged(blockpos, this, pos);
			worldIn.notifyNeighborsOfStateExcept(blockpos, this, direction.getOpposite());
		} else if (side == ToggleLatchSide.RIGHT) {
			Direction direction = state.get(HORIZONTAL_FACING).rotateYCCW();
			BlockPos blockpos = pos.offset(direction);
			if (ForgeEventFactory
					.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), EnumSet.of(direction), false)
					.isCanceled()) {
				return;
			}
			worldIn.neighborChanged(blockpos, this, pos);
			worldIn.notifyNeighborsOfStateExcept(blockpos, this, direction.getOpposite());
		}
	}

	private int getDelay(BlockState state) {
		return 0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		ToggleLatchSide side = stateIn.get(POWERED_SIDE);
		if (side == ToggleLatchSide.LEFT) {
			this.spawnParticle(stateIn, worldIn, pos, rand, true);
		} else if (side == ToggleLatchSide.RIGHT) {
			this.spawnParticle(stateIn, worldIn, pos, rand, false);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void spawnParticle(BlockState state, World world, BlockPos pos, Random rand, boolean left) {
		double xOff = left ? 0.25D : 0.75D;
		Direction direction = state.get(HORIZONTAL_FACING);
		double x = (double) pos.getX() + xOff + (rand.nextDouble() - 0.5D) * 0.2D;
		double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
		double z = (double) pos.getZ() + 0.65D + (rand.nextDouble() - 0.5D) * 0.2D;
		float f = -5.0F / 16.0F;
		double xOffset = (double) (f * (float) direction.getXOffset());
		double zOffset = (double) (f * (float) direction.getZOffset());
		world.addParticle(RedstoneParticleData.REDSTONE_DUST, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
	}

	private int calculateInputStrength(World worldIn, BlockPos pos, BlockState state) {
		Direction direction = state.get(HORIZONTAL_FACING);
		BlockPos blockpos = pos.offset(direction);
		int i = worldIn.getRedstonePower(blockpos, direction);
		if (i >= 15) {
			return i;
		} else {
			BlockState blockstate = worldIn.getBlockState(blockpos);
			return Math.max(i, blockstate.matchesBlock(Blocks.REDSTONE_WIRE) ? blockstate.get(RedstoneWireBlock.POWER) : 0);
		}
	}

}
