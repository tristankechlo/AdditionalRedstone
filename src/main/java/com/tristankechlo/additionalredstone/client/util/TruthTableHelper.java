package com.tristankechlo.additionalredstone.client.util;

import com.google.common.collect.Lists;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public final class TruthTableHelper {

    private static final List<Block> TRUTHTABLE_GATES = Lists.newArrayList(
            ModBlocks.AND_GATE_BLOCK.get(), ModBlocks.NAND_GATE_BLOCK.get(), ModBlocks.OR_GATE_BLOCK.get(),
            ModBlocks.NOR_GATE_BLOCK.get(), ModBlocks.XOR_GATE_BLOCK.get(), ModBlocks.XNOR_GATE_BLOCK.get()
    );

    private static final List<IFormattableTextComponent> GATE_NAMES = TRUTHTABLE_GATES.stream().map(Block::getName).collect(Collectors.toList());

    public static ThreeInputLogicGate get(int index) {
        return (ThreeInputLogicGate) TRUTHTABLE_GATES.get(index);
    }

    public static int getIndexOf(ThreeInputLogicGate block) {
        return TRUTHTABLE_GATES.indexOf(block);
    }

    public static ItemStack getAsItemStack(int index) {
        return TRUTHTABLE_GATES.get(index).asItem().getDefaultInstance();
    }

    public static IFormattableTextComponent getAsComponent(int index) {
        return GATE_NAMES.get(index);
    }

}
