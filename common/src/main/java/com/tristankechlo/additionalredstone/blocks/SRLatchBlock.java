package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

public class SRLatchBlock extends BaseDiodeBlock {

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        boolean isPowered = state.getValue(POWERED);
        boolean shouldBeOn = this.shouldTurnOn(worldIn, pos, state);

        if (isPowered && !shouldBeOn) {
            worldIn.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
            this.updateNeighborsInFront(worldIn, pos, state);
        } else if (!isPowered && shouldBeOn) {
            worldIn.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
            this.updateNeighborsInFront(worldIn, pos, state);
        }
    }

    @Override
    protected void checkTickOnNeighbor(Level worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(worldIn, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            worldIn.scheduleTick(pos, this, this.getDelay(state), tickpriority);
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
