package com.tristankechlo.additionalredstone.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

	public static final ItemGroup GENERAL = new ItemGroup("additionalredstone.main") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get());
		}

	};
}
