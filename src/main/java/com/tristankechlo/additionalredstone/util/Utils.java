package com.tristankechlo.additionalredstone.util;

import javax.annotation.Nullable;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

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

	public static boolean and(boolean a, boolean b, boolean c) {
		return a && b && c;
	}

	public static boolean nand(boolean a, boolean b, boolean c) {
		return !and(a, b, c);
	}

	public static boolean or(boolean a, boolean b, boolean c) {
		return a || b || c;
	}

	public static boolean nor(boolean a, boolean b, boolean c) {
		return !or(a, b, c);
	}

	public static boolean xor(boolean a, boolean b, boolean c) {
		return a ^ b ^ c;
	}

	public static boolean xnor(boolean a, boolean b, boolean c) {
		return !xor(a, b, c);
	}

	public static boolean and(boolean a, boolean b) {
		return a && b;
	}

	public static boolean nand(boolean a, boolean b) {
		return !and(a, b);
	}

	public static boolean or(boolean a, boolean b) {
		return a || b;
	}

	public static boolean nor(boolean a, boolean b) {
		return !or(a, b);
	}

	public static boolean xor(boolean a, boolean b) {
		return a ^ b;
	}

	public static boolean xnor(boolean a, boolean b) {
		return !xor(a, b);
	}

}
