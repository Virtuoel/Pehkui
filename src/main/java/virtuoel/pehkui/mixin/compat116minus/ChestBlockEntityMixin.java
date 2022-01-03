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
	@Unique
	float viewerSearchRange = 5.0F;
	
	@Shadow(remap = false)
	int field_11928;
	
	@Override
	public float pehkui_getViewerSearchRange()
	{
		return viewerSearchRange;
	}
	
	@Inject(at = @At("HEAD"), method = "method_5435(Lnet/minecraft/class_1657;)V", remap = false)
	private void onOnInvOpen(PlayerEntity player, CallbackInfo info)
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
	
	@Inject(at = @At("HEAD"), method = "method_5432(Lnet/minecraft/class_1657;)V", remap = false)
	private void onOnInvClose(PlayerEntity player, CallbackInfo info)
	{
		if (field_11928 <= 1)
		{
			field_11928 = 1;
			
			viewerSearchRange = 5.0F;
		}
	}
	
	@ModifyConstant(method = "method_17765(Lnet/minecraft/class_1937;Lnet/minecraft/class_2624;III)I", constant = @Constant(floatValue = 5.0F), remap = false)
	private float countViewersModifyDistance(float value, World world, LockableContainerBlockEntity container, int x, int y, int z)
	{
		if (container instanceof ViewerCountingBlockEntityExtensions)
		{
			return ((ViewerCountingBlockEntityExtensions) container).pehkui_getViewerSearchRange();
		}
		
		return value;
	}
}
