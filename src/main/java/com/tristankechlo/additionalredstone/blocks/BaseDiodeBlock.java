package com.tristankechlo.additionalredstone.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BaseDiodeBlock extends DiodeBlock {

	private static final VoxelShape BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	private static final VoxelShape SHAPE_N = Shapes.join(BASE, Block.box(4.0D, 2.0D, 3.0D, 12.0D, 4.0D, 11.0D),
			BooleanOp.OR);
	private static final VoxelShape SHAPE_S = Shapes.join(BASE, Block.box(4.0D, 2.0D, 5.0D, 12.0D, 4.0D, 13.0D),
			BooleanOp.OR);
	private static final VoxelShape SHAPE_E = Shapes.join(BASE, Block.box(5.0D, 2.0D, 4.0D, 13.0D, 4.0D, 12.0D),
			BooleanOp.OR);
	private static final VoxelShape SHAPE_W = Shapes.join(BASE, Block.box(3.0D, 2.0D, 4.0D, 11.0D, 4.0D, 12.0D),
			BooleanOp.OR);

	public BaseDiodeBlock() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.getDefaultDiodeState());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);
		if (facing == Direction.NORTH) {
			return SHAPE_N;
		} else if (facing == Direction.EAST) {
			return SHAPE_E;
		} else if (facing == Direction.SOUTH) {
			return SHAPE_S;
		}
		return SHAPE_W;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	protected int getRedstonePowerForSide(Level worldIn, BlockPos pos, Direction direction) {
		BlockPos blockpos = pos.relative(direction);
		int i = worldIn.getSignal(blockpos, direction);
		if (i >= 15) {
			return i;
		} else {
			BlockState state = worldIn.getBlockState(blockpos);
			return Math.max(i, state.is(Blocks.REDSTONE_WIRE) ? state.getValue(RedStoneWireBlock.POWER) : 0);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		return state.setValue(POWERED, Boolean.valueOf(this.shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state)));
	}

	protected BlockState getDefaultDiodeState() {
		return this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(POWERED)) {
			Direction direction = stateIn.getValue(FACING);
			double x = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
			double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			float f = -5.0F / 16.0F;
			double xOffset = (double) (f * (float) direction.getStepX());
			double zOffset = (double) (f * (float) direction.getStepZ());
			worldIn.addParticle(DustParticleOptions.REDSTONE, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState p_60584_) {
		return PushReaction.DESTROY;
	}

}
