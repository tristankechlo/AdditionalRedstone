package com.tristankechlo.additionalredstone.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.block.state.BlockState;

public class SRLatchBlock extends BaseDiodeBlock {

// TODO connection check
//	@Override
//	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
//		return side != state.getValue(FACING).getOpposite();
//	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		boolean isPowered = state.getValue(POWERED);
		boolean shouldBeOn = this.shouldTurnOn(worldIn, pos, state);

		if (isPowered && !shouldBeOn) {
			worldIn.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
			this.updateNeighborsInFront(worldIn, pos, state);
			return;
		} else if (!isPowered && shouldBeOn) {
			worldIn.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
			this.updateNeighborsInFront(worldIn, pos, state);
			return;
		}
	}

	@Override
	protected void checkTickOnNeighbor(Level worldIn, BlockPos pos, BlockState state) {
		if (!worldIn.getBlockTicks().willTickThisTick(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			if (this.shouldPrioritize(worldIn, pos, state)) {
				tickpriority = TickPriority.EXTREMELY_HIGH;
			}
			worldIn.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
		Direction set = state.getValue(FACING).getClockWise();
		Direction reset = state.getValue(FACING).getCounterClockWise();
		boolean setPowered = this.getRedstonePowerForSide(worldIn, pos, set) > 0;
		boolean resetPowered = this.getRedstonePowerForSide(worldIn, pos, reset) > 0;
		if (setPowered) {
			return true;
		} else if (resetPowered) {
			return false;
		}
		return state.getValue(POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

}
