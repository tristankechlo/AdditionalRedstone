package com.tristankechlo.additionalredstone.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public class Utils {

    public static final int TEXT_COLOR_SCREEN = 0xCCCCCC;

    @Nullable
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTicker(Level level,
                                                                                                   BlockEntityType<A> typeA, BlockEntityType<E> typeB, BlockEntityTicker<? super E> ticker) {
        if (level.isClientSide) {
            return null;
        }
        return typeB == typeA ? (BlockEntityTicker<A>) ticker : null;
    }

}
