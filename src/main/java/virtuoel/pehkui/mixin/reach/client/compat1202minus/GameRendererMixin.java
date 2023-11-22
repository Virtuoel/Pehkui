package virtuoel.pehkui.mixin.reach.client.compat1202minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = GameRenderer.class, priority = 990)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyConstant(method = "updateTargetedEntity", require = 0, expect = 0, constant = @Constant(doubleValue = 3.0D))
	private double pehkui$updateTargetedEntity$distance(double value, float tickDelta)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getEntityReachScale(entity, tickDelta);
			
			if (scale != 1.0F)
			{
				return scale * value;
			}
		}
		
		return value;
	}
}
