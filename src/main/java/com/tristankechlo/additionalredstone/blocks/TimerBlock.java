package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.client.screen.TimerScreen;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import com.tristankechlo.additionalredstone.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TimerBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(4.0D, 2.0D, 4.0D, 12.0D, 3.0D, 12.0D));
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public TimerBlock() {
        super(Properties.copy(Blocks.REPEATER));
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
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
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(worldIn, pos)) {
            BlockEntity tileentity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
            dropResources(state, worldIn, pos, tileentity);
            worldIn.removeBlock(pos, false);
            for (Direction direction : Direction.values()) {
                worldIn.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof TimerBlockEntity && worldIn.isClientSide) {
            TimerBlockEntity timer = (TimerBlockEntity) tile;
            int powerUp = timer.getPowerUpTime();
            int powerDown = timer.getPowerDownTime();
            int interval = timer.getInterval();
            this.openTimerScreen(powerUp, powerDown, interval, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openTimerScreen(int powerUp, int powerDown, int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new TimerScreen(powerUp, powerDown, interval, pos));
    }

    public static void setPowered(BlockState state, Level level, BlockPos pos, boolean powered) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(POWERED, powered), 3);
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return Utils.createTicker(level, type, ModBlockEntities.TIMER_BLOCK_ENTITY.get(), TimerBlockEntity::tick);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

}
