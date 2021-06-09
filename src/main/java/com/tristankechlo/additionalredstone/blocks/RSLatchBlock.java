package com.tristankechlo.additionalredstone.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RSLatchBlock extends BaseDiodeBlock {

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return side != state.get(HORIZONTAL_FACING).getOpposite();
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		boolean isPowered = state.get(POWERED);
		boolean shouldBeOn = this.shouldBePowered(worldIn, pos, state);

		if (isPowered && !shouldBeOn) {
			worldIn.setBlockState(pos, state.with(POWERED, Boolean.FALSE), 2);
			this.notifyNeighbors(worldIn, pos, state);
			return;
		} else if (!isPowered && shouldBeOn) {
			worldIn.setBlockState(pos, state.with(POWERED, Boolean.TRUE), 2);
			this.notifyNeighbors(worldIn, pos, state);
			return;
		}
	}

	@Override
	protected void updateState(World worldIn, BlockPos pos, BlockState state) {
		if (!worldIn.getPendingBlockTicks().isTickPending(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
				tickpriority = TickPriority.EXTREMELY_HIGH;
			}
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state) {
		Direction set = state.get(HORIZONTAL_FACING).rotateY();
		Direction reset = state.get(HORIZONTAL_FACING).rotateYCCW();
		boolean setPowered = this.getRedstonePowerForSide(worldIn, pos, set) > 0;
		boolean resetPowered = this.getRedstonePowerForSide(worldIn, pos, reset) > 0;
		if (resetPowered) {
			return false;
		} else if (setPowered) {
			return true;
		}
		return state.get(POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

}
