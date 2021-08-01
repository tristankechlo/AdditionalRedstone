package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.client.screen.OscillatorScreen;
import com.tristankechlo.additionalredstone.tileentity.OscillatorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OscillatorBlock extends Block {

	private static final VoxelShape SHAPE = VoxelShapes.join(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(4.5D, 2.0D, 4.5D, 11.5D, 12.0D, 11.5D), IBooleanFunction.OR);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public OscillatorBlock() {
		super(Properties.copy(Blocks.REPEATER));
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (!state.canSurvive(worldIn, pos)) {
			TileEntity tileentity = state.hasTileEntity() ? worldIn.getBlockEntity(pos) : null;
			dropResources(state, worldIn, pos, tileentity);
			worldIn.removeBlock(pos, false);
			for (Direction direction : Direction.values()) {
				worldIn.updateNeighborsAt(pos.relative(direction), this);
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canSupportRigidBlock(worldIn, pos.below());
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		TileEntity tile = worldIn.getBlockEntity(pos);
		if (tile instanceof OscillatorTileEntity && worldIn.isClientSide) {
			OscillatorTileEntity oscillator = (OscillatorTileEntity) tile;
			int ticksOn = oscillator.getTicksOn();
			int ticksOff = oscillator.getTicksOff();
			this.openOscillatorScreen(ticksOn, ticksOff, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	private void openOscillatorScreen(int ticksOn, int ticksOff, BlockPos pos) {
		Minecraft.getInstance().setScreen(new OscillatorScreen(ticksOn, ticksOff, pos));
	}

	public static void setPowered(BlockState state, World world, BlockPos pos, boolean powered) {
		world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(powered)), 3);
	}

	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new OscillatorTileEntity();
	}

}
