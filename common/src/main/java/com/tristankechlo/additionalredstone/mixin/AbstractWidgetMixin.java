package com.tristankechlo.additionalredstone.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetMixin {

    @Accessor("focused")
    void setFocused(boolean focus);

}
