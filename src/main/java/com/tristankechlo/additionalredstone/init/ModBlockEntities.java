package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, AdditionalRedstone.MOD_ID);

	public static final RegistryObject<BlockEntityType<OscillatorBlockEntity>> OSCILLATOR_TILE_ENTITY = TILE_ENTITIES
			.register("oscillator", () -> BlockEntityType.Builder
					.of(OscillatorBlockEntity::new, ModBlocks.OSCILLATOR_BLOCK.get()).build(null));

	public static final RegistryObject<BlockEntityType<TimerBlockEntity>> TIMER_TILE_ENTITY = TILE_ENTITIES.register(
			"timer", () -> BlockEntityType.Builder.of(TimerBlockEntity::new, ModBlocks.TIMER_BLOCK.get()).build(null));

	public static final RegistryObject<BlockEntityType<SequencerBlockEntity>> SEQUENCER_TILE_ENTITY = TILE_ENTITIES
			.register("sequencer", () -> BlockEntityType.Builder
					.of(SequencerBlockEntity::new, ModBlocks.SEQUENCER_BLOCK.get()).build(null));

	public static final RegistryObject<BlockEntityType<TFlipFlopBlockEntity>> T_FLIP_FLOP_TILE_ENTITY = TILE_ENTITIES
			.register("t_flip_flop", () -> BlockEntityType.Builder
					.of(TFlipFlopBlockEntity::new, ModBlocks.T_FLIP_FLOP_BLOCK.get()).build(null));
}
