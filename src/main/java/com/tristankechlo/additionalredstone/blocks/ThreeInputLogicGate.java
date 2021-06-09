package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.util.TriFunction;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThreeInputLogicGate extends BaseDiodeBlock {

	private final TriFunction<Boolean, Boolean, Boolean, Boolean> logic;

	public ThreeInputLogicGate(TriFunction<Boolean, Boolean, Boolean, Boolean> logic) {
		this.logic = logic;
	}

	@Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state) {
		Direction input = state.get(HORIZONTAL_FACING);
		Direction left = state.get(HORIZONTAL_FACING).rotateY();
		Direction right = state.get(HORIZONTAL_FACING).rotateYCCW();
		boolean i = this.getRedstonePowerForSide(worldIn, pos, input) > 0;
		boolean j = this.getRedstonePowerForSide(worldIn, pos, left) > 0;
		boolean k = this.getRedstonePowerForSide(worldIn, pos, right) > 0;
		return this.logic.apply(i, j, k);
	}

}
