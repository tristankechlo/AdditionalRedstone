package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DimmableRedstoneLampBlock extends Block {

    private static final IntegerProperty POWER = BlockStateProperties.POWER;

    public DimmableRedstoneLampBlock() {
        super(Properties.copy(Blocks.REDSTONE_LAMP).lightLevel(DimmableRedstoneLampBlock::getLightLevel));
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(POWER, context.getLevel().getBestNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos pos2, boolean $$5) {
        if (!level.isClientSide()) {
            int oldValue = state.getValue(POWER);
            int newValue = level.getBestNeighborSignal(pos);
            if (oldValue != newValue) { // input power level changed
                if (oldValue >= 1) {
                    level.getBlockTicks().scheduleTick(pos, this, 4); // changes are not instant, but after a 4 tick delay
                } else {
                    level.setBlock(pos, state.setValue(POWER, newValue), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
        int newValue = level.getBestNeighborSignal(pos);
        if (state.getValue(POWER) != newValue) {
            level.setBlock(pos, state.setValue(POWER, newValue), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    private static int getLightLevel(BlockState state) {
        if (state.hasProperty(POWER)) {
            return state.getValue(POWER);
        }
        return 0;
    }

}

