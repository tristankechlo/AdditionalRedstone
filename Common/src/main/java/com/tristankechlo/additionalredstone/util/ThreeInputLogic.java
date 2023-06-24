package com.tristankechlo.additionalredstone.util;

@FunctionalInterface
public interface ThreeInputLogic {

    boolean apply(boolean a, boolean b, boolean c);

    static boolean and(boolean a, boolean b, boolean c) {
        return a && b && c;
    }

    static boolean nand(boolean a, boolean b, boolean c) {
        return !and(a, b, c);
    }

    static boolean or(boolean a, boolean b, boolean c) {
        return a || b || c;
    }

    static boolean nor(boolean a, boolean b, boolean c) {
        return !or(a, b, c);
    }

    static boolean xor(boolean a, boolean b, boolean c) {
        return a ^ b ^ c;
    }

    static boolean xnor(boolean a, boolean b, boolean c) {
        return !xor(a, b, c);
    }

}
