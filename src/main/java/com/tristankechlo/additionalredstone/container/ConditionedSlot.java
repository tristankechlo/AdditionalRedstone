package com.tristankechlo.additionalredstone.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ConditionedSlot extends Slot {

    private final Item item;

    public ConditionedSlot(Container container, int i, int x, int y, Item mayPlace) {
        super(container, i, x, y);
        this.item = mayPlace;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem().equals(this.item);
    }

}
