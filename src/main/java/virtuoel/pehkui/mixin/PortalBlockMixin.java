package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;

@Mixin(NetherPortalBlock.class)
public abstract class PortalBlockMixin
{
	@Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
	private void onOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info)
	{
		if (PehkuiConfig.COMMON.accurateNetherPortals.get())
		{
			if (!entity.getBoundingBox().intersects(((PehkuiBlockStateExtensions) state).pehkui_getOutlineShape(world, pos).getBoundingBox().offset(pos)))
			{
				info.cancel();
			}
		}
	}
}
