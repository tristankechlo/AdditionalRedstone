package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blocks.*;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import com.tristankechlo.additionalredstone.util.ThreeInputLogic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(BuiltInRegistries.BLOCK, Constants.MOD_ID);

    public static final RegistryObject<Block> CIRCUIT_BASE_BLOCK = BLOCKS.register("circuit_base", CircuitBaseBlock::new);
    public static final RegistryObject<Block> CIRCUIT_MAKER_BLOCK = BLOCKS.register("circuit_maker", CircuitMakerBlock::new);
    public static final RegistryObject<Block> OSCILLATOR_BLOCK = BLOCKS.register("oscillator", OscillatorBlock::new);
    public static final RegistryObject<Block> TIMER_BLOCK = BLOCKS.register("timer", TimerBlock::new);

    public static final RegistryObject<Block> AND_GATE_BLOCK = BLOCKS.register("and_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::and));
    public static final RegistryObject<Block> NAND_GATE_BLOCK = BLOCKS.register("nand_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::nand));
    public static final RegistryObject<Block> OR_GATE_BLOCK = BLOCKS.register("or_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::or));
    public static final RegistryObject<Block> NOR_GATE_BLOCK = BLOCKS.register("nor_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::nor));
    public static final RegistryObject<Block> XOR_GATE_BLOCK = BLOCKS.register("xor_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::xor));
    public static final RegistryObject<Block> XNOR_GATE_BLOCK = BLOCKS.register("xnor_gate", () -> new ThreeInputLogicGate(ThreeInputLogic::xnor));
    public static final RegistryObject<Block> NOT_GATE_BLOCK = BLOCKS.register("not_gate", NotGateBlock::new);

    public static final RegistryObject<Block> T_FLIP_FLOP_BLOCK = BLOCKS.register("t_flip_flop", TFlipFlopBlock::new);
    public static final RegistryObject<Block> TOGGLE_LATCH_BLOCK = BLOCKS.register("toggle_latch", ToggleLatchBlock::new);
    public static final RegistryObject<Block> SR_LATCH_BLOCK = BLOCKS.register("sr_latch", SRLatchBlock::new);
    public static final RegistryObject<Block> RS_LATCH_BLOCK = BLOCKS.register("rs_latch", RSLatchBlock::new);
    public static final RegistryObject<Block> SEQUENCER_BLOCK = BLOCKS.register("sequencer", SequencerBlock::new);

    public static void load() {}

}
