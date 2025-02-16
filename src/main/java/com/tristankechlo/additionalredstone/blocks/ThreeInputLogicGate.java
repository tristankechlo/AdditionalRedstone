package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.client.screen.TruthtableScreen;
import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ThreeInputLogicGate extends BaseDiodeBlock {

    public final ThreeInputLogic logic;

    public ThreeInputLogicGate(ThreeInputLogic logic) {
        this.logic = logic;
    }

    @Override
    protected boolean shouldTurnOn(World level, BlockPos pos, BlockState state) {
        Direction input = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean i = getRedstonePowerRelative(level, pos, input) > 0;
        boolean j = getRedstonePowerRelative(level, pos, left) > 0;
        boolean k = getRedstonePowerRelative(level, pos, right) > 0;
        return this.logic.apply(i, j, k);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (level.isClientSide() && player.getItemInHand(hand).isEmpty()) {
            openTruthtableScreen(this);
            return ActionResultType.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @OnlyIn(Dist.CLIENT)
    private void openTruthtableScreen(ThreeInputLogicGate block) {
        Minecraft.getInstance().setScreen(new TruthtableScreen(block));
    }

}
