package com.tristankechlo.additionalredstone.container;

import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import com.tristankechlo.additionalredstone.util.Circuits;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CircuitMakerContainer extends Container {

	private final IWorldPosCallable worldPos;
	private final IntReferenceHolder selectedRecipe = IntReferenceHolder.single();
	private Runnable changeListener = () -> {
	};
	private final Slot slotStoneSlab1;
	private final Slot slotStoneSlab2;
	private final Slot slotStoneSlab3;
	private final Slot slotRedstoneTorch;
	private final Slot slotRedstone;
	private final Slot slotQuartz;
	private final Slot slotOutput;
	private final IInventory inputInventory = new Inventory(6) {
		@Override
		public void markDirty() {
			super.markDirty();
			CircuitMakerContainer.this.onCraftMatrixChanged(this);
			CircuitMakerContainer.this.changeListener.run();
		}
	};
	private final IInventory outputInventory = new Inventory(1) {
		@Override
		public void markDirty() {
			super.markDirty();
			CircuitMakerContainer.this.changeListener.run();
		}
	};

	public CircuitMakerContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(id, playerInventory, IWorldPosCallable.DUMMY);
	}

	public CircuitMakerContainer(int id, PlayerInventory playerInventory, final IWorldPosCallable worldCallable) {
		super(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), id);
		this.worldPos = worldCallable;

		this.slotRedstone = this.addSlot(new Slot(this.inputInventory, 0, 8, 25) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.REDSTONE);
			}
		});
		this.slotQuartz = this.addSlot(new Slot(this.inputInventory, 1, 28, 25) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.QUARTZ);
			}
		});
		this.slotRedstoneTorch = this.addSlot(new Slot(this.inputInventory, 2, 48, 25) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.REDSTONE_TORCH);
			}
		});
		this.slotStoneSlab1 = this.addSlot(new Slot(this.inputInventory, 3, 8, 45) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.STONE_SLAB);
			}
		});
		this.slotStoneSlab2 = this.addSlot(new Slot(this.inputInventory, 4, 28, 45) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.STONE_SLAB);
			}
		});
		this.slotStoneSlab3 = this.addSlot(new Slot(this.inputInventory, 5, 48, 45) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.STONE_SLAB);
			}
		});

		this.slotOutput = this.addSlot(new Slot(this.outputInventory, 0, 164, 50) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				CircuitMakerContainer.this.slotStoneSlab1.decrStackSize(1);
				CircuitMakerContainer.this.slotStoneSlab2.decrStackSize(1);
				CircuitMakerContainer.this.slotStoneSlab3.decrStackSize(1);
				CircuitMakerContainer.this.slotQuartz.decrStackSize(1);
				CircuitMakerContainer.this.slotRedstoneTorch.decrStackSize(1);
				CircuitMakerContainer.this.slotRedstone.decrStackSize(1);
				if (!CircuitMakerContainer.this.slotStoneSlab1.getHasStack()
						|| !CircuitMakerContainer.this.slotStoneSlab2.getHasStack()
						|| !CircuitMakerContainer.this.slotStoneSlab3.getHasStack()
						|| !CircuitMakerContainer.this.slotQuartz.getHasStack()
						|| !CircuitMakerContainer.this.slotRedstoneTorch.getHasStack()
						|| !CircuitMakerContainer.this.slotRedstone.getHasStack()) {
					CircuitMakerContainer.this.selectedRecipe.set(0);
				}
				return super.onTake(thePlayer, stack);
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

		this.trackInt(this.selectedRecipe);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.worldPos, playerIn, ModBlocks.CIRCUIT_MAKER_BLOCK.get());
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
	public boolean enchantItem(PlayerEntity playerIn, int id) {
		if (id > 0 && id <= Circuits.SIZE) {
			this.selectedRecipe.set(id);
			this.createOutputStack();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.worldPos.consume((world, pos) -> {
			this.clearContainer(playerIn, playerIn.world, this.inputInventory);
		});
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		ItemStack baseStack1 = this.slotStoneSlab1.getStack();
		ItemStack baseStack2 = this.slotStoneSlab2.getStack();
		ItemStack baseStack3 = this.slotStoneSlab3.getStack();
		ItemStack quartzStack = this.slotQuartz.getStack();
		ItemStack torchStack = this.slotRedstoneTorch.getStack();
		ItemStack redstoneStack = this.slotRedstone.getStack();

		if (baseStack1.isEmpty() || quartzStack.isEmpty() || torchStack.isEmpty() || baseStack2.isEmpty()
				|| baseStack3.isEmpty() || redstoneStack.isEmpty()) {
			this.slotOutput.putStack(ItemStack.EMPTY);
			this.selectedRecipe.set(0);
		}

		this.createOutputStack();
		this.detectAndSendChanges();
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 0 && index < 7) {
				if (!this.mergeItemStack(itemstack1, 7, 43, true)) {
					return ItemStack.EMPTY;
				}
			} else if (itemstack1.getItem() == Items.REDSTONE) {
				if (!this.mergeItemStack(itemstack1, 0, 1, true)) {
					return ItemStack.EMPTY;
				}
			} else if (itemstack1.getItem() == Items.QUARTZ) {
				if (!this.mergeItemStack(itemstack1, 1, 2, true)) {
					return ItemStack.EMPTY;
				}
			} else if (itemstack1.getItem() == Items.REDSTONE_TORCH) {
				if (!this.mergeItemStack(itemstack1, 2, 3, true)) {
					return ItemStack.EMPTY;
				}
			} else if (itemstack1.getItem() == Items.STONE_SLAB) {
				if (!this.mergeItemStack(itemstack1, 3, 6, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
			this.detectAndSendChanges();
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
			if (!ItemStack.areItemStacksEqual(possibleOutput, this.slotOutput.getStack())) {
				this.slotOutput.putStack(possibleOutput);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotCircuitBase1() {
		return this.slotStoneSlab1;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotCircuitBase2() {
		return this.slotStoneSlab2;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotCircuitBase3() {
		return this.slotStoneSlab3;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotQuartz() {
		return this.slotQuartz;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotRedstoneTorch() {
		return this.slotRedstoneTorch;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotRedstone() {
		return this.slotRedstone;
	}

	@OnlyIn(Dist.CLIENT)
	public Slot getSlotOutput() {
		return this.slotOutput;
	}

	public boolean hasEnoughItemsInSlots() {
		return !this.slotStoneSlab1.getStack().isEmpty() && !this.slotQuartz.getStack().isEmpty()
				&& !this.slotRedstoneTorch.getStack().isEmpty() && !this.slotStoneSlab2.getStack().isEmpty()
				&& !this.slotStoneSlab3.getStack().isEmpty() && !this.slotRedstone.getStack().isEmpty();
	}

}
