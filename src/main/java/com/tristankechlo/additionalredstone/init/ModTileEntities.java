package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<TileEntityType<OscillatorTileEntity>> OSCILLATOR_TILE_ENTITY = TILE_ENTITIES.register("oscillator", () -> TileEntityType.Builder.of(OscillatorTileEntity::new, ModBlocks.OSCILLATOR_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<TimerTileEntity>> TIMER_TILE_ENTITY = TILE_ENTITIES.register("timer", () -> TileEntityType.Builder.of(TimerTileEntity::new, ModBlocks.TIMER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<SequencerTileEntity>> SEQUENCER_TILE_ENTITY = TILE_ENTITIES.register("sequencer", () -> TileEntityType.Builder.of(SequencerTileEntity::new, ModBlocks.SEQUENCER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<TFlipFlopTileEntity>> T_FLIP_FLOP_TILE_ENTITY = TILE_ENTITIES.register("t_flip_flop", () -> TileEntityType.Builder.of(TFlipFlopTileEntity::new, ModBlocks.T_FLIP_FLOP_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<SupergateTileEntity>> SUPERGATE_TILE_ENTITY = TILE_ENTITIES.register("supergate", () -> TileEntityType.Builder.of(SupergateTileEntity::new, ModBlocks.SUPERGATE_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<LightDetectorTileEntity>> LIGHT_DETECTOR_TILE_ENTITY = TILE_ENTITIES.register("light_detector", () -> TileEntityType.Builder.of(LightDetectorTileEntity::new, ModBlocks.LIGHT_DETECTOR_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<ToggleLatchTileEntity>> TOGGLE_LATCH_TILE_ENTITY = TILE_ENTITIES.register("toggle_latch", () -> TileEntityType.Builder.of(ToggleLatchTileEntity::new, ModBlocks.TOGGLE_LATCH_BLOCK.get()).build(null));

}
