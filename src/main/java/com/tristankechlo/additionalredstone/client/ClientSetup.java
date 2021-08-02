package com.tristankechlo.additionalredstone.client;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AdditionalRedstone.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event) {
		MenuScreens.register(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TIMER_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.OSCILLATOR_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.NOT_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.AND_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.NAND_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.OR_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.NOR_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.XOR_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.XNOR_GATE_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.T_FLIP_FLOP_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOGGLE_LATCH_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SR_LATCH_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RS_LATCH_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CIRCUIT_MAKER_BLOCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SEQUENCER_BLOCK.get(), RenderType.cutout());
	}
}
