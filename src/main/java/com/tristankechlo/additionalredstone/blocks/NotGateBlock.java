package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NotGateBlock extends BaseDiodeBlock {

	@Override
	protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
		return this.getInputSignal(worldIn, pos, state) <= 0;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return DiodeBlock.SHAPE;
	}

// TODO connection check
//	@Override
//	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
//		Direction front = state.getValue(FACING);
//		Direction back = state.getValue(FACING).getOpposite();
//		return side == front || side == back;
//	}

}
