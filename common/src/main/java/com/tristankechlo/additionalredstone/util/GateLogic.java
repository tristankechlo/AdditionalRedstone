package com.tristankechlo.additionalredstone.util;

import net.minecraft.util.StringRepresentable;

public enum GateLogic implements StringRepresentable {

    AND("and", ThreeInputLogic::and),
    NAND("nand", ThreeInputLogic::nand),
    OR("or", ThreeInputLogic::or),
    NOR("nor", ThreeInputLogic::nor),
    XOR("xor", ThreeInputLogic::xor),
    XNOR("xnor", ThreeInputLogic::xnor);

    @SuppressWarnings("deprecation")
    public static final EnumCodec<GateLogic> CODEC = StringRepresentable.fromEnum(GateLogic::values);

    private final String name;
    private final ThreeInputLogic logic;

    GateLogic(String name, ThreeInputLogic logic) {
        this.name = name;
        this.logic = logic;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean apply(boolean a, boolean b, boolean c) {
        return this.logic.apply(a, b, c);
    }

}
