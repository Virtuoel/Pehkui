package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(NetherPortalBlock.class)
public abstract class PortalBlockMixin extends Block
{
	private PortalBlockMixin()
	{
		super(null);
	}
	
	@Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
	private void onOnEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1, CallbackInfo info)
	{
		if(!entity_1.getBoundingBox().intersects(blockState_1.getOutlineShape(world_1, blockPos_1).getBoundingBox().offset(blockPos_1)))
		{
			info.cancel();
		}
	}
}
