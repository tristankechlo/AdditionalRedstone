package com.tristankechlo.additionalredstone.container;

import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import com.tristankechlo.additionalredstone.util.Circuits;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CircuitMakerContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess worldPos;
    private final DataSlot selectedRecipe = DataSlot.standalone();
    private Runnable changeListener = () -> {};
    private final Slot slotStoneSlab1;
    private final Slot slotStoneSlab2;
    private final Slot slotStoneSlab3;
    private final Slot slotRedstoneTorch;
    private final Slot slotRedstone;
    private final Slot slotQuartz;
    private final Slot slotOutput;
    private final Container inputInventory = new SimpleContainer(6) {
        @Override
        public void setChanged() {
            super.setChanged();
            CircuitMakerContainer.this.slotsChanged(this);
            CircuitMakerContainer.this.changeListener.run();
        }
    };
    private final Container outputInventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            CircuitMakerContainer.this.changeListener.run();
        }
    };

    public CircuitMakerContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public CircuitMakerContainer(int id, Inventory playerInventory, final ContainerLevelAccess worldCallable) {
        super(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), id);
        this.worldPos = worldCallable;

        this.slotRedstone = this.addSlot(new ConditionedSlot(this.inputInventory, 0, 8, 25, Items.REDSTONE));
        this.slotQuartz = this.addSlot(new ConditionedSlot(this.inputInventory, 1, 28, 25, Items.QUARTZ));
        this.slotRedstoneTorch = this.addSlot(new ConditionedSlot(this.inputInventory, 2, 48, 25, Items.REDSTONE_TORCH));
        this.slotStoneSlab1 = this.addSlot(new ConditionedSlot(this.inputInventory, 3, 8, 45, Items.STONE_SLAB));
        this.slotStoneSlab2 = this.addSlot(new ConditionedSlot(this.inputInventory, 4, 28, 45, Items.STONE_SLAB));
        this.slotStoneSlab3 = this.addSlot(new ConditionedSlot(this.inputInventory, 5, 48, 45, Items.STONE_SLAB));

        this.slotOutput = this.addSlot(new Slot(this.outputInventory, 0, 164, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                CircuitMakerContainer.this.slotStoneSlab1.remove(1);
                CircuitMakerContainer.this.slotStoneSlab2.remove(1);
                CircuitMakerContainer.this.slotStoneSlab3.remove(1);
                CircuitMakerContainer.this.slotQuartz.remove(1);
                CircuitMakerContainer.this.slotRedstoneTorch.remove(1);
                CircuitMakerContainer.this.slotRedstone.remove(1);
                if (!CircuitMakerContainer.this.slotStoneSlab1.hasItem()
                        || !CircuitMakerContainer.this.slotStoneSlab2.hasItem()
                        || !CircuitMakerContainer.this.slotStoneSlab3.hasItem()
                        || !CircuitMakerContainer.this.slotQuartz.hasItem()
                        || !CircuitMakerContainer.this.slotRedstoneTorch.hasItem()
                        || !CircuitMakerContainer.this.slotRedstone.hasItem()) {
                    CircuitMakerContainer.this.selectedRecipe.set(0);
                }
                super.onTake(thePlayer, stack);
            }
        });

        // player inv
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 16 + j * 18, 85 + i * 18));
            }
        }
        // player hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 16 + k * 18, 143));
        }

        this.addDataSlot(this.selectedRecipe);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPos, playerIn, ModBlocks.CIRCUIT_MAKER_BLOCK.get());
    }

    @OnlyIn(Dist.CLIENT)
    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void setInventoryChangeListener(Runnable run) {
        this.changeListener = run;
    }

    @Override
    public boolean clickMenuButton(Player playerIn, int id) {
        if (id > 0 && id <= Circuits.SIZE) {
            this.selectedRecipe.set(id);
            this.createOutputStack();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.worldPos.execute((world, pos) -> {
            this.clearContainer(playerIn, this.inputInventory);
        });
    }

    @Override
    public void slotsChanged(Container inventoryIn) {
        ItemStack baseStack1 = this.slotStoneSlab1.getItem();
        ItemStack baseStack2 = this.slotStoneSlab2.getItem();
        ItemStack baseStack3 = this.slotStoneSlab3.getItem();
        ItemStack quartzStack = this.slotQuartz.getItem();
        ItemStack torchStack = this.slotRedstoneTorch.getItem();
        ItemStack redstoneStack = this.slotRedstone.getItem();

        if (baseStack1.isEmpty() || quartzStack.isEmpty() || torchStack.isEmpty() || baseStack2.isEmpty()
                || baseStack3.isEmpty() || redstoneStack.isEmpty()) {
            this.slotOutput.set(ItemStack.EMPTY);
            this.selectedRecipe.set(0);
        }

        this.createOutputStack();
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= 0 && index < 7) {
                if (!this.moveItemStackTo(itemstack1, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.REDSTONE) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.QUARTZ) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.REDSTONE_TORCH) {
                if (!this.moveItemStackTo(itemstack1, 2, 3, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.STONE_SLAB) {
                if (!this.moveItemStackTo(itemstack1, 3, 6, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    private void createOutputStack() {
        if (this.selectedRecipe.get() > 0) {
            ItemStack possibleOutput = ItemStack.EMPTY;
            if (this.hasEnoughItemsInSlots()) {
                Item item = Circuits.values()[this.selectedRecipe.get() - 1].getItem();
                possibleOutput = new ItemStack(item, 1);
            }
            if (!ItemStack.matches(possibleOutput, this.slotOutput.getItem())) {
                this.slotOutput.set(possibleOutput);
            }
        }
    }

    public boolean hasEnoughItemsInSlots() {
        return !this.slotStoneSlab1.getItem().isEmpty() && !this.slotQuartz.getItem().isEmpty()
                && !this.slotRedstoneTorch.getItem().isEmpty() && !this.slotStoneSlab2.getItem().isEmpty()
                && !this.slotStoneSlab3.getItem().isEmpty() && !this.slotRedstone.getItem().isEmpty();
    }

}
