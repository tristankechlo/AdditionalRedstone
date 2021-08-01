package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, AdditionalRedstone.MOD_ID);

	public static final RegistryObject<ContainerType<CircuitMakerContainer>> CIRCUIT_MAKER_CONTAINER = CONTAINER_TYPES
			.register("circuit_maker", () -> IForgeContainerType.create(CircuitMakerContainer::new));
}
