package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ThreeInputLogicGate extends BaseDiodeBlock {

    private final ThreeInputLogic logic;

    public ThreeInputLogicGate(ThreeInputLogic logic) {
        this.logic = logic;
    }

    @Override
    protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
        Direction input = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean i = this.getRedstonePowerForSide(worldIn, pos, input) > 0;
        boolean j = this.getRedstonePowerForSide(worldIn, pos, left) > 0;
        boolean k = this.getRedstonePowerForSide(worldIn, pos, right) > 0;
        return this.logic.apply(i, j, k);
    }

}
