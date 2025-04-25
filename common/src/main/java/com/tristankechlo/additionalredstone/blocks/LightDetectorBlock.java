package com.tristankechlo.additionalredstone.blocks;

import com.mojang.serialization.MapCodec;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.LightDetectorBlockEntity;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightDetectorBlock extends BaseEntityBlock {

    private static final IntegerProperty POWER = BlockStateProperties.POWER;
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    public static final MapCodec<LightDetectorBlock> CODEC = MapCodec.unit(LightDetectorBlock::new);

    public LightDetectorBlock() {
        super(Properties.ofFullCopy(Blocks.DAYLIGHT_DETECTOR));
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWER);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    private static void tickEntity(Level level, BlockPos pos, BlockState state, LightDetectorBlockEntity entity) {
        if (level.getGameTime() % 20L == 0L) {
            int skyLevel = skyLevel(level, pos);
            int blockLevel = level.getBrightness(LightLayer.BLOCK, pos);
            int value = Math.max(skyLevel, blockLevel);
            value = Mth.clamp(value, 0, 15);

            level.setBlock(pos, state.setValue(POWER, value), 3);
        }
    }

    private static int skyLevel(Level level, BlockPos pos) {
        if (!level.dimensionType().hasSkyLight()) {
            return 0;
        }
        int value = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        float sunAngle = level.getSunAngle(1.0F);
        if (value > 0) {
            float v = sunAngle < (float) Math.PI ? 0.0F : (float) (Math.PI * 2);
            sunAngle += (v - sunAngle) * 0.2F;
            value = Math.round((float) value * Mth.cos(sunAngle));
        }
        return Mth.clamp(value, 0, 15);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightDetectorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return AdditionalRedstone.createTicker(level, type, ModBlockEntities.LIGHT_DETECTOR_BLOCK_ENTITY.get(), LightDetectorBlock::tickEntity);
    }

}
