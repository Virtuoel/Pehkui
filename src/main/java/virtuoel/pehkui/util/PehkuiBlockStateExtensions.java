package virtuoel.pehkui.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public interface PehkuiBlockStateExtensions
{
	VoxelShape pehkui_getOutlineShape(BlockView world, BlockPos pos);
	
	Block pehkui_getBlock();
}
