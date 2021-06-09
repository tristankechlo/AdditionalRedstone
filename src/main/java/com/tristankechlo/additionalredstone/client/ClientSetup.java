package com.tristankechlo.additionalredstone.client;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AdditionalRedstone.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);

		RenderTypeLookup.setRenderLayer(ModBlocks.TIMER_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.OSCILLATOR_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.NOT_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.AND_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.NAND_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.OR_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.NOR_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.XOR_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.XNOR_GATE_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.T_FLIP_FLOP_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TOGGLE_LATCH_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SR_LATCH_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RS_LATCH_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CIRCUIT_MAKER_BLOCK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SEQUENCER_BLOCK.get(), RenderType.getCutout());
	}
}
