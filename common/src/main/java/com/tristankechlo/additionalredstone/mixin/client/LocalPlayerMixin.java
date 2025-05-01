package com.tristankechlo.additionalredstone.mixin.client;

import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.screen.*;
import com.tristankechlo.additionalredstone.util.LocalPlayerAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements LocalPlayerAddon {

    @Final
    @Shadow
    protected Minecraft minecraft;

    @Override
    public void openOscillatorScreen$AdditionalRedstone(int ticksOn, int ticksOff, BlockPos pos) {
        this.minecraft.setScreen(new OscillatorScreen(ticksOn, ticksOff, pos));
    }

    @Override
    public void openSequencerScreen$AdditionalRedstone(int interval, BlockPos pos) {
        this.minecraft.setScreen(new SequencerScreen(interval, pos));
    }

    @Override
    public void openSupergateScreen$AdditionalRedstone(byte configuration, BlockPos pos) {
        this.minecraft.setScreen(new SupergateScreen(configuration, pos));
    }

    @Override
    public void openTimerScreen$AdditionalRedstone(int powerUp, int powerDown, int interval, BlockPos pos) {
        this.minecraft.setScreen(new TimerScreen(powerUp, powerDown, interval, pos));
    }

    @Override
    public void openTruthtableScreen$AdditionalRedstone(ThreeInputLogicGate block) {
        this.minecraft.setScreen(new TruthtableScreen(block));
    }

}
