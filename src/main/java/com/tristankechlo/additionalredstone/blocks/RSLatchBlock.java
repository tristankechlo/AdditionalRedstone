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
		return side != state.getValue(FACING).getOpposite();
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
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
	protected void checkTickOnNeighbor(World worldIn, BlockPos pos, BlockState state) {
		if (!worldIn.getBlockTicks().willTickThisTick(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			if (this.shouldPrioritize(worldIn, pos, state)) {
				tickpriority = TickPriority.EXTREMELY_HIGH;
			}
			worldIn.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	protected boolean shouldTurnOn(World worldIn, BlockPos pos, BlockState state) {
		Direction set = state.getValue(FACING).getClockWise();
		Direction reset = state.getValue(FACING).getCounterClockWise();
		boolean setPowered = this.getRedstonePowerForSide(worldIn, pos, set) > 0;
		boolean resetPowered = this.getRedstonePowerForSide(worldIn, pos, reset) > 0;
		if (resetPowered) {
			return false;
		} else if (setPowered) {
			return true;
		}
		return state.getValue(POWERED);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

}
