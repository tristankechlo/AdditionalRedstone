package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.OscillatorBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OscillatorBlockEntity extends BlockEntity {

    private boolean powered = false;
    private int tickCounter = 0;
    private int ticksOn = 50;
    private int ticksOff = 50;

    public OscillatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OSCILLATOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OscillatorBlockEntity blockEntity) {
        if (!level.isClientSide && pos.equals(blockEntity.worldPosition)) {
            blockEntity.tick();
        }
    }

    private void tick() {
        if (this.level != null) {
            if (this.ticksOn <= 0) {
                this.ticksOn = 0;
                if (this.powered) {
                    this.updatePower(false);
                }
                return;
            }
            if (this.ticksOff <= 0) {
                this.ticksOff = 0;
                if (!this.powered) {
                    this.updatePower(true);
                }
                return;
            }
            if (this.powered) {
                if (this.tickCounter >= this.ticksOn) {
                    this.tickCounter = 0;
                    this.updatePower(false);
                } else {
                    this.tickCounter++;
                }
            } else {
                if (this.tickCounter >= this.ticksOff) {
                    this.tickCounter = 0;
                    this.updatePower(true);
                } else {
                    this.tickCounter++;
                }
            }
        }
    }

    private void updatePower(boolean powered) {
        BlockState blockstate = this.getBlockState();
        Block block = blockstate.getBlock();
        if (block instanceof OscillatorBlock) {
            this.powered = powered;
            OscillatorBlock.setPowered(blockstate, this.level, this.worldPosition, powered);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.tickCounter = nbt.getInt("TickCounter");
        this.powered = nbt.getBoolean("Powered");
        this.ticksOn = nbt.getInt("TicksOn");
        this.ticksOff = nbt.getInt("TicksOff");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putInt("TickCounter", this.tickCounter);
        nbt.putBoolean("Powered", this.powered);
        nbt.putInt("TicksOn", this.ticksOn);
        nbt.putInt("TicksOff", this.ticksOff);
        super.saveAdditional(nbt);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    public int getTicksOn() {
        return this.ticksOn;
    }

    public int getTicksOff() {
        return this.ticksOff;
    }

    public void setConfiguration(int ticksOn, int ticksOff) {
        this.ticksOn = ticksOn;
        this.ticksOff = ticksOff;
        this.tickCounter = 0;
    }

}
