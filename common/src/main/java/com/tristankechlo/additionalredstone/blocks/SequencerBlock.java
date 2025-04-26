package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SequencerBlock extends Block implements EntityBlock {

    private static final IntegerProperty POWERED_SIDE = IntegerProperty.create("output", 0, 3);

    public SequencerBlock() {
        super(Properties.ofFullCopy(Blocks.REPEATER));
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED_SIDE, 0));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!player.getAbilities().mayBuild) {
                return InteractionResult.PASS;
            } else {
                level.setBlock(pos, state.cycle(POWERED_SIDE), 3);
                this.playSound(player, level, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        BlockEntity tile = level.getBlockEntity(pos);
        if ((tile instanceof SequencerBlockEntity sequencer) && level.isClientSide) {
            int interval = sequencer.getInterval();
            IPlatformHelper.INSTANCE.openSequencerScreen(interval, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void playSound(Player player, LevelAccessor level, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CircuitBaseBlock.BASE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        int direction = ctx.getHorizontalDirection().getOpposite().get2DDataValue();
        return this.defaultBlockState().setValue(POWERED_SIDE, direction);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED_SIDE);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.getSignal(state, level, pos, direction);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return direction.get2DDataValue() == state.getValue(POWERED_SIDE) ? 15 : 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(level, pos)) {
            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            dropResources(state, level, pos, blockEntity);
            level.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SequencerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return AdditionalRedstone.createTicker(level, type, ModBlockEntities.SEQUENCER_BLOCK_ENTITY.get(), SequencerBlockEntity::tick);
    }

    public static void updatePower(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.cycle(POWERED_SIDE), 3);
        }
    }

}
