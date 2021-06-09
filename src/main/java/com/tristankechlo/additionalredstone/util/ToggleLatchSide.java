package com.tristankechlo.additionalredstone.util;

import net.minecraft.util.IStringSerializable;

public enum ToggleLatchSide implements IStringSerializable {
	LEFT("left"), RIGHT("right");

	private final String name;

	private ToggleLatchSide(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getString() {
		return this.name;
	}

}