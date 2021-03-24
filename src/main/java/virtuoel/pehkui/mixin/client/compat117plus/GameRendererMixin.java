package virtuoel.pehkui.mixin.client.compat117plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyConstant(method = "getBasicProjectionMatrix(D)Lnet/minecraft/util/math/Matrix4f;", constant = @Constant(floatValue = 0.05F))
	private float getBasicProjectionMatrixModifyDepth(float value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getWidthScale(entity, client.getTickDelta());
			
			if (scale < 1.0F)
			{
				return Math.max(
					(float) PehkuiConfig.CLIENT.minimumCameraDepth.get().doubleValue(),
					value * scale
				);
			}
		}
		
		return value;
	}
}
