package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.ToggleLatchBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ToggleLatchBlockEntity extends BlockEntity {

    private boolean previousInput;

    public ToggleLatchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TOGGLE_LATCH_BLOCK_ENTITY.get(), pos, state);
    }

    public boolean shouldBePowered(boolean input) {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof ToggleLatchBlock)) {
            return false;
        }
        if (input && !this.previousInput) {
            this.previousInput = input;
            return true;
        }
        this.previousInput = input;
        return false;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.previousInput = nbt.getBoolean("PreviousInput");
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putBoolean("PreviousInput", this.previousInput);
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

}
