package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.client.screen.SupergateScreen;
import com.tristankechlo.additionalredstone.tileentity.SupergateTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class SupergateBlock extends BaseDiodeBlock {

    @Override
    public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
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
    protected void checkTickOnNeighbor(World level, BlockPos pos, BlockState state) {
        if (!level.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, pos, state)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            }
            level.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
        }
    }

    protected boolean shouldTurnOn(World level, BlockPos pos, BlockState state) {
        Direction middle = state.getValue(FACING);
        Direction left = state.getValue(FACING).getClockWise();
        Direction right = state.getValue(FACING).getCounterClockWise();
        boolean m = BaseDiodeBlock.getRedstonePowerRelative(level, pos, middle) > 0;
        boolean l = BaseDiodeBlock.getRedstonePowerRelative(level, pos, left) > 0;
        boolean r = BaseDiodeBlock.getRedstonePowerRelative(level, pos, right) > 0;
        TileEntity blockEntity = state.hasTileEntity() ? level.getBlockEntity(pos) : null;
        if (blockEntity instanceof SupergateTileEntity) {
            return SupergateTileEntity.shouldBePowered((SupergateTileEntity) blockEntity, l, m, r);
        }
        return SupergateTileEntity.shouldBePowered(null, l, m, r);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = level.getBlockEntity(pos);
        if ((tile instanceof SupergateTileEntity) && level.isClientSide) {
            SupergateTileEntity blockEntity = (SupergateTileEntity) tile;
            byte config = blockEntity.getConfiguration();
            openSupergateScreen(config, pos);
        }
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openSupergateScreen(byte config, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SupergateScreen(config, pos));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SupergateTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
