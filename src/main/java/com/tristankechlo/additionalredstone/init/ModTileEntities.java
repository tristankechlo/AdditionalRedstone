package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.tileentity.OscillatorTileEntity;
import com.tristankechlo.additionalredstone.tileentity.SequencerTileEntity;
import com.tristankechlo.additionalredstone.tileentity.TFlipFlopTileEntity;
import com.tristankechlo.additionalredstone.tileentity.TimerTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, AdditionalRedstone.MOD_ID);

	public static final RegistryObject<TileEntityType<OscillatorTileEntity>> OSCILLATOR_TILE_ENTITY = 
			TILE_ENTITIES.register("oscillator", () -> TileEntityType.Builder.create(OscillatorTileEntity::new, ModBlocks.OSCILLATOR_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<TimerTileEntity>> TIMER_TILE_ENTITY = 
			TILE_ENTITIES.register("timer", () -> TileEntityType.Builder.create(TimerTileEntity::new, ModBlocks.TIMER_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<SequencerTileEntity>> SEQUENCER_TILE_ENTITY = 
			TILE_ENTITIES.register("sequencer", () -> TileEntityType.Builder.create(SequencerTileEntity::new, ModBlocks.SEQUENCER_BLOCK.get()).build(null));

	public static final RegistryObject<TileEntityType<TFlipFlopTileEntity>> T_FLIP_FLOP_TILE_ENTITY = 
			TILE_ENTITIES.register("t_flip_flop", () -> TileEntityType.Builder.create(TFlipFlopTileEntity::new, ModBlocks.T_FLIP_FLOP_BLOCK.get()).build(null));
}
