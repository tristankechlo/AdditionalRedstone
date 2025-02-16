package com.tristankechlo.additionalredstone.tileentity;

import com.tristankechlo.additionalredstone.blocks.LightDetectorBlock;
import com.tristankechlo.additionalredstone.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class LightDetectorTileEntity extends TileEntity implements ITickableTileEntity {

    public LightDetectorTileEntity() {
        super(ModTileEntities.LIGHT_DETECTOR_TILE_ENTITY.get());
    }

    @Override
    public void tick() {
        BlockState state = this.getBlockState();
        if (level.getGameTime() % 20L == 0L) {
            int skyLevel = skyLevel(level, worldPosition);
            int blockLevel = level.getBrightness(LightType.BLOCK, worldPosition);
            int value = Math.max(skyLevel, blockLevel);
            value = MathHelper.clamp(value, 0, 15);

            LightDetectorBlock.setPowered(state, level, worldPosition, value);
        }
    }

    private static int skyLevel(World level, BlockPos pos) {
        int value = level.getBrightness(LightType.SKY, pos) - level.getSkyDarken();
        float sunAngle = level.getSunAngle(1.0F);
        if (value > 0) {
            float v = sunAngle < (float) Math.PI ? 0.0F : (float) (Math.PI * 2);
            sunAngle += (v - sunAngle) * 0.2F;
            value = Math.round((float) value * MathHelper.cos(sunAngle));
        }
        return MathHelper.clamp(value, 0, 15);
    }

}
