package com.tristankechlo.additionalredstone.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class BaseDiodeBlock extends RedstoneDiodeBlock {

	private static final VoxelShape BASE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	private static final VoxelShape SHAPE_N = VoxelShapes.combineAndSimplify(BASE,
			Block.makeCuboidShape(4.0D, 2.0D, 3.0D, 12.0D, 4.0D, 11.0D), IBooleanFunction.OR);
	private static final VoxelShape SHAPE_S = VoxelShapes.combineAndSimplify(BASE,
			Block.makeCuboidShape(4.0D, 2.0D, 5.0D, 12.0D, 4.0D, 13.0D), IBooleanFunction.OR);
	private static final VoxelShape SHAPE_E = VoxelShapes.combineAndSimplify(BASE,
			Block.makeCuboidShape(5.0D, 2.0D, 4.0D, 13.0D, 4.0D, 12.0D), IBooleanFunction.OR);
	private static final VoxelShape SHAPE_W = VoxelShapes.combineAndSimplify(BASE,
			Block.makeCuboidShape(3.0D, 2.0D, 4.0D, 11.0D, 4.0D, 12.0D), IBooleanFunction.OR);

	public BaseDiodeBlock() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.getDefaultDiodeState());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction facing = state.get(HORIZONTAL_FACING);
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
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, POWERED);
	}

	protected int getRedstonePowerForSide(World worldIn, BlockPos pos, Direction direction) {
		BlockPos blockpos = pos.offset(direction);
		int i = worldIn.getRedstonePower(blockpos, direction);
		if (i >= 15) {
			return i;
		} else {
			BlockState state = worldIn.getBlockState(blockpos);
			return Math.max(i, state.matchesBlock(Blocks.REDSTONE_WIRE) ? state.get(RedstoneWireBlock.POWER) : 0);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		return state.with(POWERED, Boolean.valueOf(this.shouldBePowered(ctx.getWorld(), ctx.getPos(), state)));
	}

	protected BlockState getDefaultDiodeState() {
		return this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, Boolean.FALSE);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(POWERED)) {
			Direction direction = stateIn.get(HORIZONTAL_FACING);
			double x = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double y = (double) pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
			double z = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			float f = -5.0F / 16.0F;
			double xOffset = (double) (f * (float) direction.getXOffset());
			double zOffset = (double) (f * (float) direction.getZOffset());
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, x + xOffset, y, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}

}
