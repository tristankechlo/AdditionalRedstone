package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.TimerBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;

public class TimerTileEntity extends TileEntity implements ITickableTileEntity {

	private int powerUpTime = 0;
	private int powerDownTime = 2000;
	private int interval = 10;
	private boolean powered;
	public static final int MIN_TIME = 0;
	public static final int MAX_TIME = 24000;

	public TimerTileEntity() {
		super(ModTileEntities.TIMER_TILE_ENTITY.get());
	}

	@Override
	public void tick() {
		if (this.level != null && !this.level.isClientSide && level.getGameTime() % this.interval == 0) {
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
            return time >= this.powerUpTime && time <= this.powerDownTime;
		} else {
            return time <= this.powerDownTime || time >= this.powerUpTime;
		}
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
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		this.powerUpTime = nbt.getInt("PowerUpTime");
		this.powerDownTime = nbt.getInt("PowerDownTime");
		this.powered = nbt.getBoolean("Powered");
		this.interval = nbt.getInt("Interval");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putInt("PowerUpTime", this.powerUpTime);
		nbt.putInt("PowerDownTime", this.powerDownTime);
		nbt.putBoolean("Powered", this.powered);
		nbt.putInt("Interval", this.interval);
		return super.save(nbt);
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
		this.powerUpTime = MathHelper.clamp(powerUp, MIN_TIME, MAX_TIME);
		this.powerDownTime = MathHelper.clamp(powerDown, MIN_TIME, MAX_TIME);
		this.interval = MathHelper.clamp(interval, 1, 1000);
	}

}
