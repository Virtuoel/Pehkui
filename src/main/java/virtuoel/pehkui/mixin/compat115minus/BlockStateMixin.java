package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;

@Mixin(BlockState.class)
public abstract class BlockStateMixin implements PehkuiBlockStateExtensions
{
	@Shadow(remap = false)
	abstract VoxelShape method_17770(BlockView world, BlockPos pos); // UNMAPPED_METHOD
	
	@Override
	public VoxelShape pehkui_getOutlineShape(BlockView world, BlockPos pos)
	{
		return method_17770(world, pos);
	}
	
	@Shadow(remap = false)
	abstract Block method_11614();
	
	@Override
	public Block pehkui_getBlock()
	{
		return method_11614();
	}
}
