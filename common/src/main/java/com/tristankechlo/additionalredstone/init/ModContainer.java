package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public final class ModContainer {

    public static final RegistrationProvider<MenuType<?>> CONTAINER_TYPES = RegistrationProvider.get(Registry.MENU, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<MenuType<CircuitMakerContainer>> CIRCUIT_MAKER_CONTAINER = CONTAINER_TYPES.register("circuit_maker", IPlatformHelper.INSTANCE.buildContainerCircuitMaker());

    public static void load() {}

}