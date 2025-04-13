package com.tristankechlo.additionalredstone.mixin.client;

import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractButton.class)
public interface AbstractButtonMixin {

    @Invoker("getTextureY")
    int getTextureY();

}
