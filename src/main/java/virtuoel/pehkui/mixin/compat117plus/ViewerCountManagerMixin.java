package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ViewerCountManager.class)
public class ViewerCountManagerMixin
{
	@Shadow
	int viewerCount;
	
	@Unique
	float viewerSearchRange = 5.0F;
	
	@Inject(at = @At("HEAD"), method = "openContainer(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
	private void onOpenContainer(PlayerEntity player, World world, BlockPos pos, BlockState state, CallbackInfo info)
	{
		if (viewerCount < 0)
		{
			viewerCount = 0;
			
			viewerSearchRange = 5.0F;
		}
		
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		if (scale != 1.0F)
		{
			final float nextRange = 5.0F * scale;
			
			if (nextRange > viewerSearchRange)
			{
				viewerSearchRange = nextRange;
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "closeContainer(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
	private void onCloseContainer(PlayerEntity player, World world, BlockPos pos, BlockState state, CallbackInfo info)
	{
		if (viewerCount <= 1)
		{
			viewerCount = 1;
			
			viewerSearchRange = 5.0F;
		}
	}
	
	@ModifyConstant(method = "getInRangeViewerCount", constant = @Constant(floatValue = 5.0F))
	private float getInRangeViewerCountModifyRange(float value)
	{
		return viewerSearchRange;
	}
}
