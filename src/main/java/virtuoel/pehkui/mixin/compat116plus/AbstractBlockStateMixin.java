package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin implements PehkuiBlockStateExtensions
{
	@Shadow
	abstract VoxelShape getOutlineShape(BlockView world, BlockPos pos);
	
	@Override
	public VoxelShape pehkui_getOutlineShape(BlockView world, BlockPos pos)
	{
		return getOutlineShape(world, pos);
	}
	
	@Shadow
	abstract Block getBlock();
	
	@Override
	public Block pehkui_getBlock()
	{
		return getBlock();
	}
}
