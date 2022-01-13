package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.ViewerCountingBlockEntityExtensions;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements ViewerCountingBlockEntityExtensions
{
	@Shadow
	int viewerCount;
	
	@Unique
	float viewerSearchRange = 5.0F;
	
	@Override
	public float pehkui_getViewerSearchRange()
	{
		return viewerSearchRange;
	}
	
	@Inject(at = @At("HEAD"), method = "onOpen(Lnet/minecraft/entity/player/PlayerEntity;)V")
	private void onOnOpen(PlayerEntity player, CallbackInfo info)
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
	
	@Inject(at = @At("HEAD"), method = "onClose(Lnet/minecraft/entity/player/PlayerEntity;)V")
	private void onOnClose(PlayerEntity player, CallbackInfo info)
	{
		if (viewerCount <= 1)
		{
			viewerCount = 1;
			
			viewerSearchRange = 5.0F;
		}
	}
	
	@ModifyConstant(method = "countViewers(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/LockableContainerBlockEntity;III)I", constant = @Constant(floatValue = 5.0F))
	private static float countViewersModifyDistance(float value, World world, LockableContainerBlockEntity container, int x, int y, int z)
	{
		if (container instanceof ViewerCountingBlockEntityExtensions)
		{
			return ((ViewerCountingBlockEntityExtensions) container).pehkui_getViewerSearchRange();
		}
		
		return value;
	}
}