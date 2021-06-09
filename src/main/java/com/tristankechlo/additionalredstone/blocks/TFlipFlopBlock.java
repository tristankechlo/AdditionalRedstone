package com.tristankechlo.additionalredstone.blocks;

import java.util.Random;

import com.tristankechlo.additionalredstone.tileentity.TFlipFlopTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TFlipFlopBlock extends BaseDiodeBlock {

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		boolean inputPowered = this.calculateInputStrength(worldIn, pos, state) > 0;
		if (inputPowered) {
			worldIn.setBlockState(pos, state.cycleValue(POWERED), 2);
		}
		this.notifyNeighbors(worldIn, pos, state);
	}

	@Override
	protected void updateState(World worldIn, BlockPos pos, BlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		boolean change = false;
		if (tileentity instanceof TFlipFlopTileEntity) {
			boolean input = this.calculateInputStrength(worldIn, pos, state) > 0;
			change = ((TFlipFlopTileEntity) tileentity).shouldBePowered(input);
		}
		if (change && !worldIn.getPendingBlockTicks().isTickPending(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
				tickpriority = TickPriority.EXTREMELY_HIGH;
			}
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state) {
		boolean inputPowered = this.calculateInputStrength(worldIn, pos, state) > 0;
		if (inputPowered) {
			return !state.get(POWERED);
		}
		return state.get(POWERED);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		Direction direction1 = state.get(HORIZONTAL_FACING);
		Direction direction2 = state.get(HORIZONTAL_FACING).getOpposite();
		return side == direction1 || side == direction2;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TFlipFlopTileEntity();
	}

	@Override
	protected int getDelay(BlockState state) {
		return 0;
	}

}
