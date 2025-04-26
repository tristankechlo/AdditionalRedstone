package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.TimerBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TimerBlockEntity extends BlockEntity {

    private int powerUpTime = 0;
    private int powerDownTime = 2000;
    private int interval = 10;
    private boolean powered;
    public static final int MIN_TIME = 0;
    public static final int MAX_TIME = 24000;

    public TimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIMER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TimerBlockEntity blockEntity) {
        if (!level.isClientSide && pos.equals(blockEntity.worldPosition)) {
            blockEntity.tick();
        }
    }

    private void tick() {
        if (this.level != null && level.getGameTime() % this.interval == 0) {
            if (powerUpTime == powerDownTime) {
                if (this.powered) {
                    this.updatePower(false);
                }
                return;
            }
            boolean targetTime = this.isInTargetTime();
            if (targetTime && !this.powered) {
                this.updatePower(true);
            } else if (!targetTime && this.powered) {
                this.updatePower(false);
            }
        }
    }

    private boolean isInTargetTime() {
        int time = (int) (level.getDayTime() % MAX_TIME);
        if (this.powerUpTime < powerDownTime) {
            if (time >= this.powerUpTime && time <= this.powerDownTime) {
                return true;
            }
        } else {
            if (time <= this.powerDownTime || time >= this.powerUpTime) {
                return true;
            }
        }
        return false;
    }

    private void updatePower(boolean powered) {
        BlockState blockstate = this.getBlockState();
        Block block = blockstate.getBlock();
        if ((block instanceof TimerBlock) && this.level != null) {
            this.powered = powered;
            TimerBlock.setPowered(blockstate, this.level, this.worldPosition, powered);
        }
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.powerUpTime = nbt.getInt("PowerUpTime");
        this.powerDownTime = nbt.getInt("PowerDownTime");
        this.powered = nbt.getBoolean("Powered");
        this.interval = nbt.getInt("Interval");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("PowerUpTime", this.powerUpTime);
        nbt.putInt("PowerDownTime", this.powerDownTime);
        nbt.putBoolean("Powered", this.powered);
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

    public int getPowerUpTime() {
        return this.powerUpTime;
    }

    public int getPowerDownTime() {
        return this.powerDownTime;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setConfiguration(int powerUp, int powerDown, int interval) {
        this.powerUpTime = Mth.clamp(powerUp, MIN_TIME, MAX_TIME);
        this.powerDownTime = Mth.clamp(powerDown, MIN_TIME, MAX_TIME);
        this.interval = Mth.clamp(interval, 1, 1000);
    }

}
