package com.tristankechlo.additionalredstone.container;

import com.google.common.collect.Lists;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.init.ModRecipes;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class CircuitMakerContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess worldPos;
    private final DataSlot selectedRecipe = DataSlot.standalone();
    private final Level level;
    private Runnable changeListener = () -> {};
    private List<CircuitMakerRecipe> recipes;
    private final Slot inputSlot1;
    private final Slot inputSlot2;
    private final Slot inputSlotCircuitBase;
    private final Slot resultSlot;
    public final Container container;
    private final ResultContainer resultContainer = new ResultContainer();
    private NonNullList<ItemStack> inputs = NonNullList.withSize(3, ItemStack.EMPTY);


    public CircuitMakerContainer(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public CircuitMakerContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public CircuitMakerContainer(int id, Inventory playerInventory, final ContainerLevelAccess worldCallable) {
        super(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), id);
        this.worldPos = worldCallable;
        this.level = playerInventory.player.level();
        this.recipes = Lists.newArrayList();

        this.container = new SimpleContainer(3) {
            @Override
            public void setChanged() {
                super.setChanged();
                CircuitMakerContainer.this.slotsChanged(this);
                CircuitMakerContainer.this.changeListener.run();
            }
        };

        this.inputSlot1 = this.addSlot(new Slot(this.container, 0, 12, 24));
        this.inputSlot2 = this.addSlot(new Slot(this.container, 1, 34, 24));
        this.inputSlotCircuitBase = this.addSlot(new ConditionedSlot(this.container, 2, 23, 46, ModItems.CIRCUIT_BASE_BLOCK_ITEM.get()));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 0, 160, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                CircuitMakerContainer.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack $$$1 = CircuitMakerContainer.this.inputSlot1.remove(1);
                ItemStack $$$2 = CircuitMakerContainer.this.inputSlot2.remove(1);
                ItemStack $$$3 = CircuitMakerContainer.this.inputSlotCircuitBase.remove(1);
                if (!$$$1.isEmpty() && !$$$2.isEmpty() && !$$$3.isEmpty()) {
                    CircuitMakerContainer.this.setupResultSlot();
                }
                //TODO play sound
                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(CircuitMakerContainer.this.inputSlot1.getItem(),
                        CircuitMakerContainer.this.inputSlot2.getItem(),
                        CircuitMakerContainer.this.inputSlotCircuitBase.getItem());
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

    public List<CircuitMakerRecipe> getRecipes() {
        return recipes;
    }

    public int getNumRecipes() {
        return recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot1.hasItem() && this.inputSlot2.hasItem()
                && this.inputSlotCircuitBase.hasItem() && !this.recipes.isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.worldPos, player, ModBlocks.CIRCUIT_MAKER_BLOCK.get());
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public void setInventoryChangeListener(Runnable run) {
        this.changeListener = run;
    }

    @Override
    public boolean clickMenuButton(Player player, int index) {
        if (this.isValidRecipeIndex(index)) {
            this.selectedRecipe.set(index);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int index) {
        return index >= 0 && index < this.recipes.size();
    }

    @Override
    public void slotsChanged(Container container) {
        ItemStack $$1 = this.inputSlot1.getItem();
        ItemStack $$2 = this.inputSlot2.getItem();
        ItemStack $$3 = this.inputSlotCircuitBase.getItem();
        if (!$$1.is(this.inputs.get(0).getItem())
                || !$$2.is(this.inputs.get(1).getItem())
                || !$$3.is(this.inputs.get(2).getItem())
        ) {
            this.inputs.set(0, $$1.copy());
            this.inputs.set(1, $$2.copy());
            this.inputs.set(2, $$3.copy());
            this.setupRecipeList(container, $$1, $$2, $$3);
        }
    }

    private void setupRecipeList(Container container, ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!stack1.isEmpty() && !stack2.isEmpty() && !stack3.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE.get(), container, this.level);
        }
    }

    private void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipe.get())) {
            CircuitMakerRecipe recipe = this.recipes.get(this.selectedRecipe.get());
            ItemStack $$1 = recipe.assemble(this.container, this.level.registryAccess());
            if ($$1.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(recipe);
                this.resultSlot.set($$1);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index >= 0 && index < 3) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, false)) { // move from inputs to player inv
                    return ItemStack.EMPTY;
                }
            } else if (index == 3) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) { // move from result to player inv
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (item == ModItems.CIRCUIT_BASE_BLOCK_ITEM.get()) {
                if (!this.moveItemStackTo(itemstack1, 2, 3, false)) { // move circuit_base from player inv to inputs
                    return ItemStack.EMPTY;
                }
            } else if (this.hasRecipe(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) { // move from player inv to inputs
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 31) {
                if (!this.moveItemStackTo(itemstack1, 31, 40, false)) { // move from player inv to hotbar
                    return ItemStack.EMPTY;
                }
            } else if (index >= 31 && index < 40 && !this.moveItemStackTo(itemstack1, 3, 31, false)) { // move from hotbar to player inv
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    private boolean hasRecipe(ItemStack stack) {
        return this.level.getRecipeManager().getAllRecipesFor(ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE.get()).stream().anyMatch((recipe) -> {
            //check if stack is used in input1 or input2
            return recipe.getInput1().test(stack) || recipe.getInput2().test(stack);
        });
    }

    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.worldPos.execute((level, pos) -> {
            this.clearContainer(player, this.container);
        });
    }

}
