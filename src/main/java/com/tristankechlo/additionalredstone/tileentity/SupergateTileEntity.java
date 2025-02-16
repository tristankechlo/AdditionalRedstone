package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.init.ModTileEntities;
import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class SupergateTileEntity extends TileEntity {

    private boolean[] configuration;

    public SupergateTileEntity() {
        super(ModTileEntities.SUPERGATE_TILE_ENTITY.get());
        this.configuration = new boolean[8];
        for (int i = 0; i < 8; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i];
            this.configuration[i] = shouldBePowered(null, input[0], input[1], input[2]);
        }
    }

    public static boolean shouldBePowered(SupergateTileEntity entity, boolean left, boolean middle, boolean right) {
        if (entity == null) {
            return ThreeInputLogic.and(left, middle, right); // default configuration when the block is placed for the first time
        }
        for (int i = 0; i < 8; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i];
            if (input[0] == left && input[1] == middle && input[2] == right) {
                return entity.configuration[i];
            }
        }
        return false;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        byte temp = tag.getByte("Configuration");
        this.configuration = byteToBooleans(temp);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        byte temp = booleansToByte(configuration);
        tag.putByte("Configuration", temp);
        return super.save(tag);
    }

    public void setConfiguration(byte configuration) {
        this.configuration = byteToBooleans(configuration);
    }

    public byte getConfiguration() {
        return booleansToByte(configuration);
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

    public static boolean[] byteToBooleans(byte b) {
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) {
            result[i] = (b & (1 << i)) != 0;
        }
        return result;
    }

    public static byte booleansToByte(boolean[] b) {
        byte result = 0;
        for (int i = 0; i < 8; i++) {
            if (b[i]) {
                result |= (byte) (1 << i);
            }
        }
        return result;
    }

}
