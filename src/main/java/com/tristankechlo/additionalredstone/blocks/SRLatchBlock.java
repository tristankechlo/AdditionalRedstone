package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class SRLatchBlock extends BaseDiodeBlock {

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return side != state.getValue(FACING).getOpposite();
    }

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
        boolean isPowered = state.getValue(POWERED);
        boolean shouldBeOn = this.shouldTurnOn(level, pos, state);

        if (isPowered && !shouldBeOn) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
            this.updateNeighborsInFront(level, pos, state);
        } else if (!isPowered && shouldBeOn) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
            this.updateNeighborsInFront(level, pos, state);
        }
    }

    @Override
    protected void checkTickOnNeighbor(World level, BlockPos pos, BlockState state) {
        if (!level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    protected boolean shouldTurnOn(World level, BlockPos pos, BlockState state) {
        Direction set = state.getValue(FACING).getClockWise();
        Direction reset = state.getValue(FACING).getCounterClockWise();
        boolean setPowered = getRedstonePowerRelative(level, pos, set) > 0;
        boolean resetPowered = getRedstonePowerRelative(level, pos, reset) > 0;
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
