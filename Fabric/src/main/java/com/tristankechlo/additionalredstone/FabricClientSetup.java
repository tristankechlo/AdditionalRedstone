package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public class FabricClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModContainer.CIRCUIT_MAKER_CONTAINER.get(), CircuitMakerScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CIRCUIT_MAKER_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OSCILLATOR_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TIMER_BLOCK.get(), RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AND_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NAND_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OR_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOR_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.XOR_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.XNOR_GATE_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOT_GATE_BLOCK.get(), RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.T_FLIP_FLOP_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TOGGLE_LATCH_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SR_LATCH_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RS_LATCH_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SEQUENCER_BLOCK.get(), RenderType.cutout());
    }

}
