package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.BiFunction;

public class TwoInputLogicGate extends BaseDiodeBlock {

    private final BiFunction<Boolean, Boolean, Boolean> logic;

    public TwoInputLogicGate(BiFunction<Boolean, Boolean, Boolean> logic) {
        this.logic = logic;
    }

    @Override
    protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean i = this.getRedstonePowerForSide(worldIn, pos, left) > 0;
        boolean j = this.getRedstonePowerForSide(worldIn, pos, right) > 0;
        return logic.apply(i, j);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return state.getValue(FACING) != side;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

}
