package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.OscillatorBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class OscillatorTileEntity extends TileEntity implements ITickableTileEntity {

	private boolean powered = false;
	private int tickCounter = 0;
	private int ticksOn = 50;
	private int ticksOff = 50;

	public OscillatorTileEntity() {
		super(ModTileEntities.OSCILLATOR_TILE_ENTITY.get());
	}

	@Override
	public void tick() {
		if (this.world != null && !this.world.isRemote) {
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
					this.updatePower(!this.powered);
				} else {
					this.tickCounter++;
				}
			} else {
				if (this.tickCounter >= this.ticksOff) {
					this.tickCounter = 0;
					this.updatePower(!this.powered);
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
			OscillatorBlock.setPowered(blockstate, this.world, this.pos, powered);
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.tickCounter = nbt.getInt("TickCounter");
		this.powered = nbt.getBoolean("Powered");
		this.ticksOn = nbt.getInt("TicksOn");
		this.ticksOff = nbt.getInt("TicksOff");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		nbt.putInt("TickCounter", this.tickCounter);
		nbt.putBoolean("Powered", this.powered);
		nbt.putInt("TicksOn", this.ticksOn);
		nbt.putInt("TicksOff", this.ticksOff);
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
