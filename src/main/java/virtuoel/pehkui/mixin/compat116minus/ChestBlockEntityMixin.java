package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.ViewerCountingBlockEntityExtensions;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements ViewerCountingBlockEntityExtensions
{
	@Dynamic @Shadow
	int field_11928; // UNMAPPED_FIELD
	
	@Unique
	float viewerSearchRange = 5.0F;
	
	@Override
	public float pehkui_getViewerSearchRange()
	{
		return viewerSearchRange;
	}
	
	@Dynamic
	@Inject(at = @At("HEAD"), method = MixinConstants.ON_OPEN)
	private void pehkui$onOpen(PlayerEntity player, CallbackInfo info)
	{
		if (field_11928 < 0)
		{
			field_11928 = 0;
			
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
	
	@Dynamic
	@Inject(at = @At("HEAD"), method = MixinConstants.ON_CLOSE)
	private void pehkui$onClose(PlayerEntity player, CallbackInfo info)
	{
		if (field_11928 <= 1)
		{
			field_11928 = 1;
			
			viewerSearchRange = 5.0F;
		}
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.COUNT_VIEWERS, at = @At(value = "CONSTANT", args = "floatValue=5.0F"))
	private static float pehkui$countViewers$distance(float value, World world, LockableContainerBlockEntity container, int x, int y, int z)
	{
		if (container instanceof ViewerCountingBlockEntityExtensions)
		{
			return ((ViewerCountingBlockEntityExtensions) container).pehkui_getViewerSearchRange();
		}
		
		return value;
	}
}
