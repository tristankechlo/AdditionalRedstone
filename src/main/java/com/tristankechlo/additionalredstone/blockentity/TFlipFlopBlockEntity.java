package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.TFlipFlopBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TFlipFlopBlockEntity extends BlockEntity {

	private boolean previousInput;

	public TFlipFlopBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.T_FLIP_FLOP_TILE_ENTITY.get(), pos, state);
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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.previousInput = nbt.getBoolean("PreviousInput");
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putBoolean("PreviousInput", this.previousInput);
		return super.save(compound);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 42, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}

}
