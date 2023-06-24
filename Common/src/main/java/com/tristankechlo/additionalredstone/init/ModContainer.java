package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainer {

    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MOD_ID);

    public static final RegistryObject<MenuType<CircuitMakerContainer>> CIRCUIT_MAKER_CONTAINER = CONTAINER_TYPES.register("circuit_maker", () -> IForgeMenuType.create(CircuitMakerContainer::new));
}
