package com.tristankechlo.additionalredstone.blocks;

import com.mojang.serialization.MapCodec;
import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;

public class SupergateBlock extends BaseDiodeBlock implements EntityBlock {

    public static final MapCodec<SupergateBlock> CODEC = MapCodec.unit(SupergateBlock::new);

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

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        Direction middle = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean m = BaseDiodeBlock.getRedstonePowerRelative(level, pos, middle) > 0;
        boolean l = BaseDiodeBlock.getRedstonePowerRelative(level, pos, left) > 0;
        boolean r = BaseDiodeBlock.getRedstonePowerRelative(level, pos, right) > 0;
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (blockEntity instanceof SuperGateBlockEntity) {
            return SuperGateBlockEntity.shouldBePowered((SuperGateBlockEntity) blockEntity, l, m, r);
        }
        return SuperGateBlockEntity.shouldBePowered(null, l, m, r);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        BlockEntity tile = level.getBlockEntity(pos);
        if ((tile instanceof SuperGateBlockEntity blockEntity) && level.isClientSide) {
            byte config = blockEntity.getConfiguration();
            IPlatformHelper.INSTANCE.openSupergateScreen(config, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SuperGateBlockEntity(pos, state);
    }

}
