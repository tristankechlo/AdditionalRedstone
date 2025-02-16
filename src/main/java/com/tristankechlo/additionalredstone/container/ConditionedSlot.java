package com.tristankechlo.additionalredstone.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ConditionedSlot extends Slot {

    private final Item item;

    public ConditionedSlot(IInventory container, int i, int x, int y, Item mayPlace) {
        super(container, i, x, y);
        this.item = mayPlace;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem().equals(this.item);
    }

}
