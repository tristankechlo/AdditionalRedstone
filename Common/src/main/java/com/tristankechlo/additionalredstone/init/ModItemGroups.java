package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final RegistrationProvider<CreativeModeTab> TABS = RegistrationProvider.get(BuiltInRegistries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("general", () -> IPlatformHelper.INSTANCE.buildCreativeModeTab()
            .icon(() -> new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get()))
            .title(Component.translatable("itemGroup.additionalredstone.main"))
            .displayItems((parameters, output) -> {
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
            })
            .build());

    public static void load() {}

}
