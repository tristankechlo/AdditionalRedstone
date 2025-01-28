package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

import java.util.Random;

public class TFlipFlopBlock extends BaseDiodeBlock implements EntityBlock {

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        boolean inputPowered = this.getInputSignal(level, pos, state) > 0;
        if (inputPowered) {
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
        this.updateNeighborsInFront(level, pos, state);
    }

    @Override
    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        BlockEntity tileentity = level.getBlockEntity(pos);
        boolean change = false;
        if (tileentity instanceof TFlipFlopBlockEntity) {
            boolean input = this.getInputSignal(level, pos, state) > 0;
            change = ((TFlipFlopBlockEntity) tileentity).shouldBePowered(input);
        }
        if (change && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        boolean inputPowered = this.getInputSignal(level, pos, state) > 0;
        if (inputPowered) {
            return !state.getValue(POWERED);
        }
        return state.getValue(POWERED);
    }

    @Override
    protected int getDelay(BlockState state) {
        return 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TFlipFlopBlockEntity(pos, state);
    }

}
