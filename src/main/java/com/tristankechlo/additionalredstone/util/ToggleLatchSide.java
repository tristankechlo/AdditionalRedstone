package com.tristankechlo.additionalredstone.util;

import net.minecraft.util.StringRepresentable;

public enum ToggleLatchSide implements StringRepresentable {

	LEFT("left"),
	RIGHT("right");

	private final String name;

	private ToggleLatchSide(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

}