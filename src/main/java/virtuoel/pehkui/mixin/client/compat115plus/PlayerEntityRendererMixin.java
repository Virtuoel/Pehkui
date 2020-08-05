package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
{
	@Inject(at = @At("RETURN"), method = "getPositionOffset", cancellable = true)
	private void onGetPositionOffset(AbstractClientPlayerEntity entity, float tickDelta, CallbackInfoReturnable<Vec3d> info)
	{
		final Vec3d ret = info.getReturnValue();
		if (ret != Vec3d.ZERO)
		{
			info.setReturnValue(ret.multiply(ScaleUtils.getHeightScale(entity, tickDelta)));
		}
	}
}
