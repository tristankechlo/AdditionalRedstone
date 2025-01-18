package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;

public final class FabricItemGroup {

    public static void register() {
        FabricItemGroupBuilder.create(new ResourceLocation(AdditionalRedstone.MOD_ID, "main"))
                .icon(() -> ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance())
                .appendItems((output) -> {
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
                    output.add(ModItems.SEQUENCER_BLOCK_ITEM.get().getDefaultInstance());
                    output.add(ModItems.TIMER_BLOCK_ITEM.get().getDefaultInstance());
                    output.add(ModItems.OSCILLATOR_BLOCK_ITEM.get().getDefaultInstance());
                    output.add(ModItems.SUPERGATE_BLOCK_ITEM.get().getDefaultInstance());
                })
                .build();
    }

}
