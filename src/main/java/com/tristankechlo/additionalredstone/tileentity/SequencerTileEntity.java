package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.SequencerBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SequencerTileEntity extends TileEntity implements ITickableTileEntity {

	private int tickCounter = 0;
	private int interval = 50;

	public SequencerTileEntity() {
		super(ModTileEntities.SEQUENCER_TILE_ENTITY.get());
	}

	@Override
	public void tick() {
		if (this.level != null && !this.level.isClientSide && this.interval > 0) {
			if (this.tickCounter >= this.interval) {
				this.tickCounter = 0;
				this.updatePower();
			} else {
				this.tickCounter++;
			}
		}
	}

	private void updatePower() {
		BlockState blockstate = this.getBlockState();
		Block block = blockstate.getBlock();
		if (block instanceof SequencerBlock) {
			SequencerBlock.update(blockstate, level, worldPosition);
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		this.tickCounter = nbt.getInt("TickCounter");
		this.interval = nbt.getInt("Interval");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putInt("TickCounter", this.tickCounter);
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

	public int getInterval() {
		return this.interval;
	}

	public void setConfiguration(int interval) {
		this.interval = interval;
		this.tickCounter = 0;
	}
}
