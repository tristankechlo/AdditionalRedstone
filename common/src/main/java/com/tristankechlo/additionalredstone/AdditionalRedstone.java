package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.init.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;
import java.util.function.Consumer;

public class AdditionalRedstone {

    public static final String MOD_NAME = "AdditionalRedstone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final String MOD_ID = "additionalredstone";
    public static final boolean[][] INPUT_STATES = new boolean[][]{{false, false, false}, {false, false, true}, {false, true, false}, {false, true, true}, {true, false, false}, {true, false, true}, {true, true, false}, {true, true, true}};
    public static boolean JEI_LOADED = false;

    public static void init() {
        ModBlockEntities.load();
        ModBlocks.load();
        ModContainer.load();
        ModItems.load();
        ModRecipes.load();
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTicker(Level level, BlockEntityType<A> typeA, BlockEntityType<E> typeB, BlockEntityTicker<? super E> ticker) {
        if (level.isClientSide()) {
            return null;
        }
        return typeB == typeA ? (BlockEntityTicker<A>) ticker : null;
    }

    public static void fillItemGroup(Consumer<ItemStack> output) {
        output.accept(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.CIRCUIT_BASE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.NOT_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.AND_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.NAND_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.OR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.NOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.XOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.XNOR_GATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.T_FLIP_FLOP_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.TOGGLE_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.SR_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.RS_LATCH_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.SUPERGATE_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.SEQUENCER_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.TIMER_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.LED.get().getDefaultInstance());
        output.accept(ModItems.OSCILLATOR_BLOCK_ITEM.get().getDefaultInstance());
        output.accept(ModItems.DIMMABLE_REDSTONE_LAMP_ITEM.get().getDefaultInstance());
        output.accept(ModItems.LIGHT_DETECTOR_ITEM.get().getDefaultInstance());
    }

}
