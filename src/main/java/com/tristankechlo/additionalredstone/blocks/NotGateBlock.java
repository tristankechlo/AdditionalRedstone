package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class NotGateBlock extends BaseDiodeBlock {

	@Override
	protected boolean shouldTurnOn(World worldIn, BlockPos pos, BlockState state) {
		return this.getInputSignal(worldIn, pos, state) <= 0;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return RedstoneDiodeBlock.SHAPE;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		Direction front = state.getValue(FACING);
		Direction back = state.getValue(FACING).getOpposite();
		return side == front || side == back;
	}

}
