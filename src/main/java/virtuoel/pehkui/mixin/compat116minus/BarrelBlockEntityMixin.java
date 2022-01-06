package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.ViewerCountingBlockEntityExtensions;

@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin implements ViewerCountingBlockEntityExtensions
{
	@Shadow(remap = false)
	int field_17583;
	
	@Unique
	float viewerSearchRange = 5.0F;
	
	@Override
	public float pehkui_getViewerSearchRange()
	{
		return viewerSearchRange;
	}
	
	@Inject(at = @At("HEAD"), method = "method_5435(Lnet/minecraft/class_1657;)V", remap = false)
	private void onOnOpen(PlayerEntity player, CallbackInfo info)
	{
		if (field_17583 < 0)
		{
			field_17583 = 0;
			
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
	
	@Inject(at = @At("HEAD"), method = "method_5432(Lnet/minecraft/class_1657;)V", remap = false)
	private void onOnClose(PlayerEntity player, CallbackInfo info)
	{
		if (field_17583 <= 1)
		{
			field_17583 = 1;
			
			viewerSearchRange = 5.0F;
		}
	}
}
