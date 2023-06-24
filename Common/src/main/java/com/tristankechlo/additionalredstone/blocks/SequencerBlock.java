package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.client.screen.SequencerScreen;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public class SequencerBlock extends BaseEntityBlock {

    public static final IntegerProperty POWERED_SIDE = IntegerProperty.create("output", 0, 3);
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public SequencerBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED_SIDE, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!player.getAbilities().mayBuild) {
                return InteractionResult.PASS;
            } else {
                worldIn.setBlock(pos, state.cycle(POWERED_SIDE), 3);
                this.playSound(player, worldIn, pos, true);
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        }
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof SequencerBlockEntity && worldIn.isClientSide) {
            SequencerBlockEntity sequencer = (SequencerBlockEntity) tile;
            int interval = sequencer.getInterval();
            this.openSequencerScreen(interval, pos);
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    private void playSound(@Nullable Player playerIn, LevelAccessor worldIn, BlockPos pos, boolean hitByArrow) {
        worldIn.playSound(playerIn, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
    }

    @OnlyIn(Dist.CLIENT)
    private void openSequencerScreen(int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SequencerScreen(interval, pos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return canSupportRigidBlock(worldIn, pos.below());
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
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return this.getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return side.get2DDataValue() == state.getValue(POWERED_SIDE) ? 15 : 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(world, pos)) {
            BlockEntity tileentity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropResources(state, world, pos, tileentity);
            world.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SequencerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.SEQUENCER_BLOCK_ENTITY.get(), SequencerBlockEntity::tick);
    }

    public static void updatePower(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.cycle(POWERED_SIDE), 3);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

}
