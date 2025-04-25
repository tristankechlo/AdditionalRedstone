package com.tristankechlo.additionalredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class DimmableRedstoneLampBlock extends Block {

    private static final IntegerProperty POWER = BlockStateProperties.POWER;

    public DimmableRedstoneLampBlock() {
        super(Properties.ofFullCopy(Blocks.REDSTONE_LAMP).lightLevel(DimmableRedstoneLampBlock::getLightLevel));
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWER, context.getLevel().getBestNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean $$5) {
        if (!level.isClientSide()) {
            int oldValue = state.getValue(POWER);
            int newValue = level.getBestNeighborSignal(pos);
            if (oldValue != newValue) { // input power level changed
                if (oldValue >= 1) {
                    level.scheduleTick(pos, this, 4); // changes are not instant, but after a 4 tick delay
                } else {
                    level.setBlock(pos, state.setValue(POWER, newValue), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int newValue = level.getBestNeighborSignal(pos);
        if (state.getValue(POWER) != newValue) {
            level.setBlock(pos, state.setValue(POWER, newValue), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    private static int getLightLevel(BlockState state) {
        if (state.hasProperty(POWER)) {
            return state.getValue(POWER);
        }
        return 0;
    }

}
