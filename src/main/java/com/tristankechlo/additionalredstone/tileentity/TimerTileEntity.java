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
	public static final int minTime = 0;
	public static final int maxTime = 24000;

	public TimerTileEntity() {
		super(ModTileEntities.TIMER_TILE_ENTITY.get());
	}

	@Override
	public void tick() {
		if (this.world != null && !this.world.isRemote && world.getGameTime() % this.interval == 0) {
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
		int time = (int) (world.getDayTime() % maxTime);
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
		if (block instanceof TimerBlock) {
			this.powered = powered;
			TimerBlock.setPowered(blockstate, this.world, this.pos, powered);
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.powerUpTime = nbt.getInt("PowerUpTime");
		this.powerDownTime = nbt.getInt("PowerDownTime");
		this.powered = nbt.getBoolean("Powered");
		this.interval = nbt.getInt("Interval");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		nbt.putInt("PowerUpTime", this.powerUpTime);
		nbt.putInt("PowerDownTime", this.powerDownTime);
		nbt.putBoolean("Powered", this.powered);
		nbt.putInt("Interval", this.interval);
		return super.write(nbt);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), 42, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState blockState = world.getBlockState(pos);
		this.read(blockState, pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
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
		this.powerUpTime = MathHelper.clamp(powerUp, minTime, maxTime);
		this.powerDownTime = MathHelper.clamp(powerDown, minTime, maxTime);
		this.interval = MathHelper.clamp(interval, 1, 1000);
	}

}
