package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(ItemFrameEntityRenderer.class)
public class ItemFrameEntityRendererMixin
{
	@Inject(at = @At("RETURN"), method = "getPositionOffset", cancellable = true)
	private void onGetPositionOffset(ItemFrameEntity entity, float tickDelta, CallbackInfoReturnable<Vec3d> info)
	{
		info.setReturnValue(info.getReturnValue().multiply(-5.0D / 48.0D, ((double) entity.getHorizontalFacing().getOffsetY()) * 0.125D, -5.0D / 48.0D));
	}
}
