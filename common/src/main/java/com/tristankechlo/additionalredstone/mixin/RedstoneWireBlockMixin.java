package com.tristankechlo.additionalredstone.mixin;

import com.tristankechlo.additionalredstone.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneWireBlockMixin {

    @Inject(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at = @At("HEAD"), cancellable = true)
    private void shouldConnectTo$AdditionalRedstone(BlockGetter world, BlockPos pos, Direction direction, boolean conductor, CallbackInfoReturnable<RedstoneSide> cir) {
        BlockPos blockpos = pos.relative(direction);
        BlockState state = world.getBlockState(blockpos);

        if (state.is(ModBlocks.NOT_GATE_BLOCK.get()) || state.is(ModBlocks.T_FLIP_FLOP_BLOCK.get())) {
            //only connect front and back
            Direction front = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
            Direction back = state.getValue(HorizontalDirectionalBlock.FACING);
            RedstoneSide result = (direction == front || direction == back) ? RedstoneSide.SIDE : RedstoneSide.NONE;
            cir.setReturnValue(result);

        } else if (state.is(ModBlocks.RS_LATCH_BLOCK.get()) || state.is(ModBlocks.SR_LATCH_BLOCK.get())) {
            //only connect left, right and back
            Direction front = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
            RedstoneSide result = (direction != front) ? RedstoneSide.SIDE : RedstoneSide.NONE;
            cir.setReturnValue(result);

        } else if (state.is(ModBlocks.TOGGLE_LATCH_BLOCK.get())) {
            //only connect left, right and front
            Direction back = state.getValue(HorizontalDirectionalBlock.FACING);
            RedstoneSide result = (direction != back) ? RedstoneSide.SIDE : RedstoneSide.NONE;
            cir.setReturnValue(result);
        }
    }

}
