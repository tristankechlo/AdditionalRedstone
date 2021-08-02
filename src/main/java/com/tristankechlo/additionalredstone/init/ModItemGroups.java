package com.tristankechlo.additionalredstone.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

	public static final CreativeModeTab GENERAL = new CreativeModeTab("additionalredstone.main") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get());
		}

	};
}
