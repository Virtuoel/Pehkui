package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{
	private PlayerEntityRendererMixin()
	{
		super(null, null, 0);
	}
	
	@Inject(at = @At("RETURN"), method = "getPositionOffset", cancellable = true)
	private void onGetPositionOffset(AbstractClientPlayerEntity entity, float tickDelta, CallbackInfoReturnable<Vec3d> info)
	{
		final Vec3d ret = info.getReturnValue();
		if (ret != Vec3d.ZERO)
		{
			info.setReturnValue(ret.multiply(ScaleData.of(entity).getScale(tickDelta)));
		}
	}
}
