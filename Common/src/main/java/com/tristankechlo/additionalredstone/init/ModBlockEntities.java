package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blockentity.*;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<OscillatorBlockEntity>> OSCILLATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("oscillator", () -> IPlatformHelper.INSTANCE.buildBETypeOscillator().build(null));
    public static final RegistryObject<BlockEntityType<TimerBlockEntity>> TIMER_BLOCK_ENTITY = BLOCK_ENTITIES.register("timer", () -> IPlatformHelper.INSTANCE.buildBlockEntityTypeTimer().build(null));
    public static final RegistryObject<BlockEntityType<SequencerBlockEntity>> SEQUENCER_BLOCK_ENTITY = BLOCK_ENTITIES.register("sequencer", () -> IPlatformHelper.INSTANCE.buildBlockEntityTypeSequencer().build(null));
    public static final RegistryObject<BlockEntityType<TFlipFlopBlockEntity>> T_FLIP_FLOP_BLOCK_ENTITY = BLOCK_ENTITIES.register("t_flip_flop", () -> IPlatformHelper.INSTANCE.buildBlockEntityTypeTFlipFlop().build(null));
    public static final RegistryObject<BlockEntityType<SuperGateBlockEntity>> SUPERGATE_BLOCK_ENTITY = BLOCK_ENTITIES.register("supergate", () -> IPlatformHelper.INSTANCE.buildBlockEntityTypeSuperGate().build(null));

    public static void load() {}

}
