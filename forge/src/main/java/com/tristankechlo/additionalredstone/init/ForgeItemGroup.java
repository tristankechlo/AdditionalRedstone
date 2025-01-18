package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ForgeItemGroup extends CreativeModeTab {

    public ForgeItemGroup() {
        super(AdditionalRedstone.MOD_ID + ".main");
    }

    @Override
    public ItemStack makeIcon() {
        return ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance();
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> output) {
        AdditionalRedstone.fillItemGroup(output);
        super.fillItemList(output);
    }

}
