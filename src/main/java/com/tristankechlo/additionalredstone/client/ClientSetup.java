package com.tristankechlo.additionalredstone.client;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModContainer;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AdditionalRedstone.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event) {
		MenuScreens.register(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);
	}

}
