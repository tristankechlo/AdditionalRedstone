package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.*;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {

    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(BuiltInRegistries.BLOCK_ENTITY_TYPE, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<BlockEntityType<OscillatorBlockEntity>> OSCILLATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("oscillator", IPlatformHelper.INSTANCE.makeBlockEntityType(OscillatorBlockEntity::new, ModBlocks.OSCILLATOR_BLOCK));
    public static final RegistryObject<BlockEntityType<TimerBlockEntity>> TIMER_BLOCK_ENTITY = BLOCK_ENTITIES.register("timer", IPlatformHelper.INSTANCE.makeBlockEntityType(TimerBlockEntity::new, ModBlocks.TIMER_BLOCK));
    public static final RegistryObject<BlockEntityType<SequencerBlockEntity>> SEQUENCER_BLOCK_ENTITY = BLOCK_ENTITIES.register("sequencer", IPlatformHelper.INSTANCE.makeBlockEntityType(SequencerBlockEntity::new, ModBlocks.SEQUENCER_BLOCK));
    public static final RegistryObject<BlockEntityType<TFlipFlopBlockEntity>> T_FLIP_FLOP_BLOCK_ENTITY = BLOCK_ENTITIES.register("t_flip_flop", IPlatformHelper.INSTANCE.makeBlockEntityType(TFlipFlopBlockEntity::new, ModBlocks.T_FLIP_FLOP_BLOCK));
    public static final RegistryObject<BlockEntityType<SuperGateBlockEntity>> SUPERGATE_BLOCK_ENTITY = BLOCK_ENTITIES.register("supergate", IPlatformHelper.INSTANCE.makeBlockEntityType(SuperGateBlockEntity::new, ModBlocks.SUPERGATE_BLOCK));
    public static final RegistryObject<BlockEntityType<LightDetectorBlockEntity>> LIGHT_DETECTOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("light_detector", IPlatformHelper.INSTANCE.makeBlockEntityType(LightDetectorBlockEntity::new, ModBlocks.LIGHT_DETECTOR_BLOCK));
    public static final RegistryObject<BlockEntityType<ToggleLatchBlockEntity>> TOGGLE_LATCH_BLOCK_ENTITY = BLOCK_ENTITIES.register("toggle_latch", IPlatformHelper.INSTANCE.makeBlockEntityType(ToggleLatchBlockEntity::new, ModBlocks.TOGGLE_LATCH_BLOCK));

    public static void load() {}

}
