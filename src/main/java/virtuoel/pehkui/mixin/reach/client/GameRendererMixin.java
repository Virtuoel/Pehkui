package virtuoel.pehkui.mixin.reach.client;

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

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;

	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0D))
	private double updateTargetedEntityModifyDistance(double value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getReachScale(entity, client.getTickDelta());
			
			if (scale > 1.0F)
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6.0D))
	private double updateTargetedEntityModifyExtendedDistance(double value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getReachScale(entity, client.getTickDelta());
			
			if (scale > 1.0F)
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0D))
	private double updateTargetedEntityModifyMaxDistance(double value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getReachScale(entity, client.getTickDelta());
			
			if (scale > 1.0F)
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
}
