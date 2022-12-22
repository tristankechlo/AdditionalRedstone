package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.*;
import com.tristankechlo.additionalredstone.util.Utils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<Block> CIRCUIT_MAKER_BLOCK = BLOCKS.register("circuit_maker", () -> new CircuitMakerBlock());
    public static final RegistryObject<Block> OSCILLATOR_BLOCK = BLOCKS.register("oscillator", () -> new OscillatorBlock());
    public static final RegistryObject<Block> TIMER_BLOCK = BLOCKS.register("timer", () -> new TimerBlock());

    public static final RegistryObject<Block> AND_GATE_BLOCK = BLOCKS.register("and_gate", () -> new ThreeInputLogicGate(Utils::and));
    public static final RegistryObject<Block> NAND_GATE_BLOCK = BLOCKS.register("nand_gate", () -> new ThreeInputLogicGate(Utils::nand));
    public static final RegistryObject<Block> OR_GATE_BLOCK = BLOCKS.register("or_gate", () -> new ThreeInputLogicGate(Utils::or));
    public static final RegistryObject<Block> NOR_GATE_BLOCK = BLOCKS.register("nor_gate", () -> new ThreeInputLogicGate(Utils::nor));
    public static final RegistryObject<Block> XOR_GATE_BLOCK = BLOCKS.register("xor_gate", () -> new ThreeInputLogicGate(Utils::xor));
    public static final RegistryObject<Block> XNOR_GATE_BLOCK = BLOCKS.register("xnor_gate", () -> new ThreeInputLogicGate(Utils::xnor));
    public static final RegistryObject<Block> NOT_GATE_BLOCK = BLOCKS.register("not_gate", () -> new NotGateBlock());

    public static final RegistryObject<Block> T_FLIP_FLOP_BLOCK = BLOCKS.register("t_flip_flop", () -> new TFlipFlopBlock());
    public static final RegistryObject<Block> TOGGLE_LATCH_BLOCK = BLOCKS.register("toggle_latch", () -> new ToggleLatchBlock());
    public static final RegistryObject<Block> SR_LATCH_BLOCK = BLOCKS.register("sr_latch", () -> new SRLatchBlock());
    public static final RegistryObject<Block> RS_LATCH_BLOCK = BLOCKS.register("rs_latch", () -> new RSLatchBlock());
    public static final RegistryObject<Block> SEQUENCER_BLOCK = BLOCKS.register("sequencer", () -> new SequencerBlock());

}
