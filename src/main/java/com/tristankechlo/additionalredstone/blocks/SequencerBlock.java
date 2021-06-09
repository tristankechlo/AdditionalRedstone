package com.tristankechlo.additionalredstone.blocks;

import javax.annotation.Nullable;

import com.tristankechlo.additionalredstone.client.screen.SequencerScreen;
import com.tristankechlo.additionalredstone.tileentity.SequencerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SequencerBlock extends Block {

	public static final IntegerProperty POWERED_SIDE = IntegerProperty.create("output", 0, 3);
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

	public SequencerBlock() {
		super(Properties.from(Blocks.REPEATER));
		this.setDefaultState(this.stateContainer.getBaseState().with(POWERED_SIDE, 0));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (player.isSneaking()) {
			if (!player.abilities.allowEdit) {
				return ActionResultType.PASS;
			} else {
				worldIn.setBlockState(pos, state.cycleValue(POWERED_SIDE), 3);
				this.playSound(player, worldIn, pos, true);
				return ActionResultType.func_233537_a_(worldIn.isRemote);
			}
		}
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof SequencerTileEntity && worldIn.isRemote) {
			SequencerTileEntity sequencer = (SequencerTileEntity) tile;
			int interval = sequencer.getInterval();
			this.openSequencerScreen(interval, pos);
		}
		return ActionResultType.func_233537_a_(worldIn.isRemote);
	}

	private void playSound(@Nullable PlayerEntity playerIn, IWorld worldIn, BlockPos pos, boolean hitByArrow) {
		worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	@OnlyIn(Dist.CLIENT)
	private void openSequencerScreen(int interval, BlockPos pos) {
		Minecraft.getInstance().displayGuiScreen(new SequencerScreen(interval, pos));
	}

	public static void update(BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, state.cycleValue(POWERED_SIDE), 3);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return hasSolidSideOnTop(worldIn, pos.down());
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		int direction = ctx.getPlacementHorizontalFacing().getOpposite().getHorizontalIndex();
		return this.getDefaultState().with(POWERED_SIDE, direction);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(POWERED_SIDE);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return this.getWeakPower(blockState, blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return side.getHorizontalIndex() == state.get(POWERED_SIDE) ? 15 : 0;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
			boolean isMoving) {
		if (!state.isValidPosition(world, pos)) {
			TileEntity tileentity = state.hasTileEntity() ? world.getTileEntity(pos) : null;
			spawnDrops(state, world, pos, tileentity);
			world.removeBlock(pos, false);
			for (Direction direction : Direction.values()) {
				world.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SequencerTileEntity();
	}
}
