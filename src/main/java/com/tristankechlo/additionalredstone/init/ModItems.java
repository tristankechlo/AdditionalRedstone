package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalRedstone.MOD_ID);

	public static final RegistryObject<Item> CIRCUIT_MAKER_BLOCK_ITEM = ModItems.ITEMS.register("circuit_maker",
			() -> new BlockItem(ModBlocks.CIRCUIT_MAKER_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> OSCILLATOR_BLOCK_ITEM = ModItems.ITEMS.register("oscillator",
			() -> new BlockItem(ModBlocks.OSCILLATOR_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> TIMER_BLOCK_ITEM = ModItems.ITEMS.register("timer",
			() -> new BlockItem(ModBlocks.TIMER_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> NOT_GATE_BLOCK_ITEM = ModItems.ITEMS.register("not_gate",
			() -> new BlockItem(ModBlocks.NOT_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> AND_GATE_BLOCK_ITEM = ModItems.ITEMS.register("and_gate",
			() -> new BlockItem(ModBlocks.AND_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> NAND_GATE_BLOCK_ITEM = ModItems.ITEMS.register("nand_gate",
			() -> new BlockItem(ModBlocks.NAND_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> OR_GATE_BLOCK_ITEM = ModItems.ITEMS.register("or_gate",
			() -> new BlockItem(ModBlocks.OR_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> NOR_GATE_BLOCK_ITEM = ModItems.ITEMS.register("nor_gate",
			() -> new BlockItem(ModBlocks.NOR_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> XOR_GATE_BLOCK_ITEM = ModItems.ITEMS.register("xor_gate",
			() -> new BlockItem(ModBlocks.XOR_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> XNOR_GATE_BLOCK_ITEM = ModItems.ITEMS.register("xnor_gate",
			() -> new BlockItem(ModBlocks.XNOR_GATE_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> T_FLIP_FLOP_BLOCK_ITEM = ModItems.ITEMS.register("t_flip_flop",
			() -> new BlockItem(ModBlocks.T_FLIP_FLOP_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> TOGGLE_LATCH_BLOCK_ITEM = ModItems.ITEMS.register("toggle_latch",
			() -> new BlockItem(ModBlocks.TOGGLE_LATCH_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> SR_LATCH_BLOCK_ITEM = ModItems.ITEMS.register("sr_latch",
			() -> new BlockItem(ModBlocks.SR_LATCH_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> RS_LATCH_BLOCK_ITEM = ModItems.ITEMS.register("rs_latch",
			() -> new BlockItem(ModBlocks.RS_LATCH_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

	public static final RegistryObject<Item> SEQUENCER_BLOCK_ITEM = ModItems.ITEMS.register("sequencer",
			() -> new BlockItem(ModBlocks.SEQUENCER_BLOCK.get(), new Properties().group(ModItemGroups.GENERAL)));

}
