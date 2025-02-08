package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SuperGateBlockEntity extends BlockEntity {

    private boolean[] configuration;

    public SuperGateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SUPERGATE_BLOCK_ENTITY.get(), pos, state);
        this.configuration = new boolean[8];
        for (int i = 0; i < 8; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i];
            this.configuration[i] = shouldBePowered(null, input[0], input[1], input[2]);
        }
    }

    public static boolean shouldBePowered(SuperGateBlockEntity entity, boolean left, boolean middle, boolean right) {
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
    public void load(CompoundTag tag) {
        super.load(tag);
        byte temp = tag.getByte("Configuration");
        this.configuration = byteToBooleans(temp);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        byte temp = booleansToByte(configuration);
        tag.putByte("Configuration", temp);
    }

    public void setConfiguration(byte configuration) {
        this.configuration = byteToBooleans(configuration);
    }

    public byte getConfiguration() {
        return booleansToByte(configuration);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
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
