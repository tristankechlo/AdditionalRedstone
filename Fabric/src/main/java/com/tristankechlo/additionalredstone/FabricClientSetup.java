package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModContainer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class FabricClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);
    }

}
