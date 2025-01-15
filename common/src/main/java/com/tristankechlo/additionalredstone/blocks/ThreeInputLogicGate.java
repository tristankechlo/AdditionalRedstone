package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ThreeInputLogicGate extends BaseDiodeBlock {

    public final ThreeInputLogic logic;

    public ThreeInputLogicGate(ThreeInputLogic logic) {
        this.logic = logic;
    }

    @Override
    protected boolean shouldTurnOn(Level worldIn, BlockPos pos, BlockState state) {
        Direction input = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean i = getRedstonePowerForSide(worldIn, pos, input) > 0;
        boolean j = getRedstonePowerForSide(worldIn, pos, left) > 0;
        boolean k = getRedstonePowerForSide(worldIn, pos, right) > 0;
        return this.logic.apply(i, j, k);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide() && player.getItemInHand(hand).isEmpty()) {
            IPlatformHelper.INSTANCE.openTruthtableScreen(this);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, result);
    }

}
