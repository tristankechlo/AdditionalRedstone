package com.tristankechlo.additionalredstone.util;

import com.tristankechlo.additionalredstone.init.ModItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum Circuits {

	AND(ModItems.AND_GATE_BLOCK_ITEM.get()),
	NAND(ModItems.NAND_GATE_BLOCK_ITEM.get()),
	OR(ModItems.OR_GATE_BLOCK_ITEM.get()),
	NOR(ModItems.NOR_GATE_BLOCK_ITEM.get()),
	NOT(ModItems.NOT_GATE_BLOCK_ITEM.get()),
	XOR(ModItems.XOR_GATE_BLOCK_ITEM.get()),
	XNOR(ModItems.XNOR_GATE_BLOCK_ITEM.get()),
	RS_LATCH(ModItems.RS_LATCH_BLOCK_ITEM.get()),
	SR_LATCH(ModItems.SR_LATCH_BLOCK_ITEM.get()),
	TOGGLE_LATCH(ModItems.TOGGLE_LATCH_BLOCK_ITEM.get()),
	T_FLIP_FLOP(ModItems.T_FLIP_FLOP_BLOCK_ITEM.get()),
	SEQUENCER(ModItems.SEQUENCER_BLOCK_ITEM.get());

	private final ItemStack stack;
	public static final int SIZE = values().length;

	private Circuits(Item item) {
		this.stack = new ItemStack(item);
	}

	public Item getItem() {
		return this.stack.getItem();
	}

	public ItemStack getItemStack() {
		return this.stack;
	}

}
