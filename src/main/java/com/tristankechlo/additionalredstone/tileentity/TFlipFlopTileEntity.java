package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.TFlipFlopBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class TFlipFlopTileEntity extends TileEntity {

	private boolean previousInput;

	public TFlipFlopTileEntity() {
		super(ModTileEntities.T_FLIP_FLOP_TILE_ENTITY.get());
	}

	public boolean shouldBePowered(boolean input) {
		BlockState state = this.getBlockState();
		if (!(state.getBlock() instanceof TFlipFlopBlock)) {
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
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.previousInput = nbt.getBoolean("PreviousInput");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putBoolean("PreviousInput", this.previousInput);
		return super.write(compound);
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

}
