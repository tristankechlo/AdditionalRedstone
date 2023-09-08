package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class SuperGateBlock extends BaseDiodeBlock implements EntityBlock {

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

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        Direction middle = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean m = BaseDiodeBlock.getRedstonePowerForSide(level, pos, middle) > 0;
        boolean l = BaseDiodeBlock.getRedstonePowerForSide(level, pos, left) > 0;
        boolean r = BaseDiodeBlock.getRedstonePowerForSide(level, pos, right) > 0;
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (blockEntity instanceof SuperGateBlockEntity) {
            return SuperGateBlockEntity.shouldBePowered((SuperGateBlockEntity) blockEntity, l, m, r);
        }
        return SuperGateBlockEntity.shouldBePowered(null, l, m, r);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return super.use(state, worldIn, pos, player, handIn, hit);
        // open gui
    }

    protected BlockState getDefaultDiodeState() {
        return this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SuperGateBlockEntity(pos, state);
    }

}
