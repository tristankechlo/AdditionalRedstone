package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.blocks.SequencerBlock;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SequencerBlockEntity extends BlockEntity {

	private int tickCounter = 0;
	private int interval = 50;

	public SequencerBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SEQUENCER_BLOCK_ENTITY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SequencerBlockEntity blockEntity) {
		if (!level.isClientSide && pos.equals(blockEntity.worldPosition)) {
			blockEntity.tick();
		}
	}

	private void tick() {
		if (this.level != null && this.interval > 0) {
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
			SequencerBlock.updatePower(blockstate, level, worldPosition);
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.tickCounter = nbt.getInt("TickCounter");
		this.interval = nbt.getInt("Interval");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		nbt.putInt("TickCounter", this.tickCounter);
		nbt.putInt("Interval", this.interval);
		super.saveAdditional(nbt);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}

	public int getInterval() {
		return this.interval;
	}

	public void setConfiguration(int interval) {
		this.interval = interval;
		this.tickCounter = 0;
	}
}
