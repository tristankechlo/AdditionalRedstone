package com.tristankechlo.additionalredstone.mixin;

import com.tristankechlo.additionalredstone.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "getFuel", at = @At("RETURN"))
    private static void getFuel$AdditionalRedstone(CallbackInfoReturnable<Map<Item, Integer>> cir) {
        cir.getReturnValue().put(ModItems.LIGHT_DETECTOR_ITEM.get(), 300); // similar to minecraft:daylight_detector
    }

}
