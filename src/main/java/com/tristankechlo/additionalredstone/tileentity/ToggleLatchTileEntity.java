package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.ToggleLatchBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class ToggleLatchTileEntity extends TileEntity {

    private boolean previousInput;

    public ToggleLatchTileEntity() {
        super(ModTileEntities.TOGGLE_LATCH_TILE_ENTITY.get());
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
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        this.previousInput = tag.getBoolean("PreviousInput");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putBoolean("PreviousInput", this.previousInput);
        return super.save(tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        save(nbt);
        return new SUpdateTileEntityPacket(this.getBlockPos(), 42, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = level.getBlockState(worldPosition);
        this.load(blockState, pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(state, tag);
    }

}
