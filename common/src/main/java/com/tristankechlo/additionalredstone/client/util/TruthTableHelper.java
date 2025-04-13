package com.tristankechlo.additionalredstone.client.util;

import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public final class TruthTableHelper {

    private static final List<Block> TRUTHTABLE_GATES = List.of(
            ModBlocks.AND_GATE_BLOCK.get(), ModBlocks.NAND_GATE_BLOCK.get(), ModBlocks.OR_GATE_BLOCK.get(),
            ModBlocks.NOR_GATE_BLOCK.get(), ModBlocks.XOR_GATE_BLOCK.get(), ModBlocks.XNOR_GATE_BLOCK.get()
    );

    private static final List<MutableComponent> GATE_NAMES = TRUTHTABLE_GATES.stream().map(Block::getName).toList();

    public static ThreeInputLogicGate get(int index) {
        return (ThreeInputLogicGate) TRUTHTABLE_GATES.get(index);
    }

    public static int getIndexOf(ThreeInputLogicGate block) {
        return TRUTHTABLE_GATES.indexOf(block);
    }

    public static ItemStack getAsItemStack(int index) {
        return TRUTHTABLE_GATES.get(index).asItem().getDefaultInstance();
    }

    public static MutableComponent getAsComponent(int index) {
        return GATE_NAMES.get(index);
    }

}
