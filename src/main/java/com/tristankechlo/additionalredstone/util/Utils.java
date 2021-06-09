package com.tristankechlo.additionalredstone.util;

public class Utils {

	public static final int TEXT_COLOR_SCREEN = 0xCCCCCC;

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
