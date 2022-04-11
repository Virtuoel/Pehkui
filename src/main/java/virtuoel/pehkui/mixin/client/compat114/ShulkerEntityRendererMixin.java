package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.entity.mob.ShulkerEntity;

@Mixin(ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin
{
	@Inject(at = @At("RETURN"), method = "method_4058", remap = false)
	private void onSetupTransforms(ShulkerEntity entity, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo info)
	{
		// TODO FIXME
	}
}
