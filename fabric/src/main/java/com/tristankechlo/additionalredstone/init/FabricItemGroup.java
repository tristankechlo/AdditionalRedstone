package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;

public final class FabricItemGroup {

    public static void register() {
        FabricItemGroupBuilder.create(new ResourceLocation(AdditionalRedstone.MOD_ID, "main"))
                .icon(() -> ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance())
                .appendItems(AdditionalRedstone::fillItemGroup)
                .build();
    }

}
