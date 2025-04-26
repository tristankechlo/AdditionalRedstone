package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.SequencerBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SequencerBlockEntity extends BlockEntity {

    private int tickCounter = 0;
    private int interval = 50;

    public SequencerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEQUENCER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SequencerBlockEntity blockEntity) {
        if (level.isClientSide || !pos.equals(blockEntity.worldPosition) || !state.is(ModBlocks.SEQUENCER_BLOCK.get())) {
            return;
        }
        if (blockEntity.level != null && blockEntity.interval > 0) {
            if (blockEntity.tickCounter >= blockEntity.interval) {
                blockEntity.tickCounter = 0;
                SequencerBlock.updatePower(state, level, blockEntity.worldPosition);
            } else {
                blockEntity.tickCounter++;
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.tickCounter = nbt.getInt("TickCounter");
        this.interval = nbt.getInt("Interval");
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("TickCounter", this.tickCounter);
        nbt.putInt("Interval", this.interval);
        super.saveAdditional(nbt, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt, provider);
        return nbt;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setConfiguration(int interval) {
        this.interval = interval;
        this.tickCounter = 0;
    }

}
