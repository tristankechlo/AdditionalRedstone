package com.tristankechlo.additionalredstone.blockentity;

import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightDetectorBlockEntity extends BlockEntity {

    public LightDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIGHT_DETECTOR_BLOCK_ENTITY.get(), pos, state);
    }

}
