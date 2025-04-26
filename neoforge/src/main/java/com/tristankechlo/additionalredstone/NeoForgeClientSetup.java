package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = AdditionalRedstone.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeClientSetup {

    @SubscribeEvent
    public static void init(final RegisterMenuScreensEvent event) {
        event.register(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);
    }

}
