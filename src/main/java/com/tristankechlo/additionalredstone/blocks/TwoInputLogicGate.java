package com.tristankechlo.additionalredstone.blocks;

import java.util.function.BiFunction;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TwoInputLogicGate extends BaseDiodeBlock {

	private final BiFunction<Boolean, Boolean, Boolean> logic;

	public TwoInputLogicGate(BiFunction<Boolean, Boolean, Boolean> logic) {
		this.logic = logic;
	}

	@Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state) {
		Direction left = state.get(HORIZONTAL_FACING).rotateY();
		Direction right = state.get(HORIZONTAL_FACING).rotateYCCW();
		boolean i = this.getRedstonePowerForSide(worldIn, pos, left) > 0;
		boolean j = this.getRedstonePowerForSide(worldIn, pos, right) > 0;
		return logic.apply(i, j);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return state.get(HORIZONTAL_FACING) != side;
	}

}
