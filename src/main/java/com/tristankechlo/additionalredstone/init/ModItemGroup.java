package com.tristankechlo.additionalredstone.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModItemGroup extends ItemGroup {

    public ModItemGroup() {
        super("additionalredstone.main");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> output) {
        output.add(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.CIRCUIT_BASE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.NOT_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.AND_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.NAND_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.OR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.NOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.XOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.XNOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.T_FLIP_FLOP_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.TOGGLE_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.SR_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.RS_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.SUPERGATE_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.SEQUENCER_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.TIMER_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.LED.get().getDefaultInstance());
        output.add(ModItems.OSCILLATOR_BLOCK_ITEM.get().getDefaultInstance());
        output.add(ModItems.DIMMABLE_REDSTONE_LAMP_ITEM.get().getDefaultInstance());
        output.add(ModItems.LIGHT_DETECTOR_ITEM.get().getDefaultInstance());
    }

}
