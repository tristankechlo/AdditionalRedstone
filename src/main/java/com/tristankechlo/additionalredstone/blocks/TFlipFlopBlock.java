package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.tileentity.TFlipFlopTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TFlipFlopBlock extends BaseDiodeBlock {

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
        boolean inputPowered = this.getInputSignal(level, pos, state) > 0;
        if (inputPowered) {
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
        this.updateNeighborsInFront(level, pos, state);
    }

    @Override
    protected void checkTickOnNeighbor(World level, BlockPos pos, BlockState state) {
        TileEntity blockEntity = level.getBlockEntity(pos);
        boolean change = false;
        if (blockEntity instanceof TFlipFlopTileEntity) {
            boolean input = this.getInputSignal(level, pos, state) > 0;
            change = ((TFlipFlopTileEntity) blockEntity).shouldBePowered(input);
        }
        if (change && !level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    @Override
    protected boolean shouldTurnOn(World level, BlockPos pos, BlockState state) {
        boolean inputPowered = this.getInputSignal(level, pos, state) > 0;
        if (inputPowered) {
            return !state.getValue(POWERED);
        }
        return state.getValue(POWERED);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        Direction direction1 = state.getValue(FACING);
        Direction direction2 = state.getValue(FACING).getOpposite();
        return side == direction1 || side == direction2;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TFlipFlopTileEntity();
    }

    @Override
    protected int getDelay(BlockState state) {
        return 0;
    }

}
