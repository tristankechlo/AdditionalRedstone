package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<Item> CIRCUIT_MAKER_BLOCK_ITEM = create("circuit_maker", ModBlocks.CIRCUIT_MAKER_BLOCK);
    public static final RegistryObject<Item> OSCILLATOR_BLOCK_ITEM = create("oscillator", ModBlocks.OSCILLATOR_BLOCK);
    public static final RegistryObject<Item> TIMER_BLOCK_ITEM = create("timer", ModBlocks.TIMER_BLOCK);
    public static final RegistryObject<Item> NOT_GATE_BLOCK_ITEM = create("not_gate", ModBlocks.NOT_GATE_BLOCK);
    public static final RegistryObject<Item> AND_GATE_BLOCK_ITEM = create("and_gate", ModBlocks.AND_GATE_BLOCK);
    public static final RegistryObject<Item> NAND_GATE_BLOCK_ITEM = create("nand_gate", ModBlocks.NAND_GATE_BLOCK);
    public static final RegistryObject<Item> OR_GATE_BLOCK_ITEM = create("or_gate", ModBlocks.OR_GATE_BLOCK);
    public static final RegistryObject<Item> NOR_GATE_BLOCK_ITEM = create("nor_gate", ModBlocks.NOR_GATE_BLOCK);
    public static final RegistryObject<Item> XOR_GATE_BLOCK_ITEM = create("xor_gate", ModBlocks.XOR_GATE_BLOCK);
    public static final RegistryObject<Item> XNOR_GATE_BLOCK_ITEM = create("xnor_gate", ModBlocks.XNOR_GATE_BLOCK);
    public static final RegistryObject<Item> T_FLIP_FLOP_BLOCK_ITEM = create("t_flip_flop", ModBlocks.T_FLIP_FLOP_BLOCK);
    public static final RegistryObject<Item> TOGGLE_LATCH_BLOCK_ITEM = create("toggle_latch", ModBlocks.TOGGLE_LATCH_BLOCK);
    public static final RegistryObject<Item> SR_LATCH_BLOCK_ITEM = create("sr_latch", ModBlocks.SR_LATCH_BLOCK);
    public static final RegistryObject<Item> RS_LATCH_BLOCK_ITEM = create("rs_latch", ModBlocks.RS_LATCH_BLOCK);
    public static final RegistryObject<Item> SEQUENCER_BLOCK_ITEM = create("sequencer", ModBlocks.SEQUENCER_BLOCK);

    private static RegistryObject<Item> create(String name, Supplier<Block> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Properties().tab(ModItemGroups.GENERAL)));
    }

}
