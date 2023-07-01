package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

public class TFlipFlopBlock extends BaseDiodeBlock implements EntityBlock {

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        boolean inputPowered = this.getInputSignal(worldIn, pos, state) > 0;
        if (inputPowered) {
            worldIn.setBlock(pos, state.cycle(POWERED), 2);
        }
        this.updateNeighborsInFront(worldIn, pos, state);
    }

    @Override
    protected void checkTickOnNeighbor(Level worldIn, BlockPos pos, BlockState state) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        boolean change = false;
        if (tileentity instanceof TFlipFlopBlockEntity) {
            boolean input = this.getInputSignal(worldIn, pos, state) > 0;
            change = ((TFlipFlopBlockEntity) tileentity).shouldBePowered(input);
        }
        if (change && !worldIn.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(worldIn, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            worldIn.scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
        boolean inputPowered = this.getInputSignal(worldIn, pos, state) > 0;
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
