package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModItemGroups {

    public static final RegistrationProvider<CreativeModeTab> TABS = RegistrationProvider.get(BuiltInRegistries.CREATIVE_MODE_TAB, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("general", () -> IPlatformHelper.INSTANCE.buildCreativeModeTab()
            .icon(() -> new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get()))
            .title(Component.translatable("itemGroup.additionalredstone.main"))
            .displayItems((parameters, output) -> AdditionalRedstone.fillItemGroup(output::accept))
            .build());

    public static void load() {}

}
