package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import virtuoel.pehkui.util.ScaleRenderUtils;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
	@Inject(method = "checkEmpty", at = @At(value = "HEAD"))
	private void pehkui$checkEmpty(MatrixStack matrices, CallbackInfo info)
	{
		if (!matrices.isEmpty())
		{
			ScaleRenderUtils.logIfItemRenderCancelled();
		}
	}
}
