package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin extends Block
{
	@Shadow @Final static VoxelShape X_SHAPE;
	@Shadow @Final static VoxelShape Z_SHAPE;
	
	private PortalBlockMixin()
	{
		super(null);
	}
	
	@Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
	private void onOnEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1, CallbackInfo info)
	{
		if(!entity_1.getBoundingBox().intersects((blockState_1.get(Properties.AXIS_XZ) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE).getBoundingBox().offset(blockPos_1)))
		{
			info.cancel();
		}
	}
}
