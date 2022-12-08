package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;

public class ModItemGroups {

    public static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(AdditionalRedstone.MOD_ID, "general"),
                builder -> builder.icon(() -> new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get()))
                        .title(Component.translatable("itemGroup.additionalredstone.main"))
                        .displayItems((features, output, hasPermissions) -> {
                            output.accept(ModItems.NOT_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.AND_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.NAND_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.OR_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.NOR_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.XOR_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.XNOR_GATE_BLOCK_ITEM.get());
                            output.accept(ModItems.T_FLIP_FLOP_BLOCK_ITEM.get());
                            output.accept(ModItems.TOGGLE_LATCH_BLOCK_ITEM.get());
                            output.accept(ModItems.SR_LATCH_BLOCK_ITEM.get());
                            output.accept(ModItems.RS_LATCH_BLOCK_ITEM.get());
                            output.accept(ModItems.SEQUENCER_BLOCK_ITEM.get());
                            output.accept(ModItems.TIMER_BLOCK_ITEM.get());
                            output.accept(ModItems.OSCILLATOR_BLOCK_ITEM.get());
                            output.accept(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get());
                        }));
    }

}
