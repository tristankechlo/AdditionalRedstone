package com.tristankechlo.additionalredstone.blocks;

import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class CircuitMakerBlock extends HorizontalBlock {

	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent(
			"container.additionalredstone.circuit_maker");
	private static final VoxelShape SHAPE = VoxelShapes.join(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(2.5D, 2.0D, 2.5D, 13.5D, 12.0D, 13.5D), IBooleanFunction.OR);

	public CircuitMakerBlock() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.METAL).strength(5F, 6F)
				.harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().noOcclusion());
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canSupportRigidBlock(worldIn, pos.below());
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
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return false;
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			player.openMenu(state.getMenuProvider(worldIn, pos));
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, playerInv, player) -> {
			return new CircuitMakerContainer(id, playerInv, IWorldPosCallable.create(worldIn, pos));
		}, CONTAINER_NAME);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
