package com.tristankechlo.additionalredstone.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

public class SRLatchBlock extends BaseDiodeBlock {

    public static final MapCodec<SRLatchBlock> CODEC = MapCodec.unit(SRLatchBlock::new);

    @Override
    protected MapCodec<? extends DiodeBlock> codec() {
        return CODEC;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
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
    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (!level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
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
