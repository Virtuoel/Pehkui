package virtuoel.pehkui.mixin.reach.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = GameRenderer.class, priority = 990)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyVariable(method = "updateTargetedEntity", require = 0, expect = 0, ordinal = 0, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/Entity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"))
	private double pehkui$updateTargetedEntity$setDistance(double value, float tickDelta)
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
	
	@ModifyVariable(method = "updateTargetedEntity", require = 0, expect = 0, ordinal = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;"))
	private double pehkui$updateTargetedEntity$squaredDistance(double value, float tickDelta)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			if (this.client.crosshairTarget == null || this.client.crosshairTarget.getType() == HitResult.Type.MISS)
			{
				final float scale = ScaleUtils.getEntityReachScale(entity, tickDelta);
				final double baseEntityReach = client.interactionManager.hasExtendedReach() ? 6.0D : client.interactionManager.getCurrentGameMode().isCreative() ? 5.0F : 4.5F;
				final double entityReach = scale * baseEntityReach;
				
				return entityReach * entityReach;
			}
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=3.0D"))
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
	
	@ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=6.0D"))
	private double pehkui$updateTargetedEntity$extendedDistance(double value, float tickDelta)
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
	
	@ModifyExpressionValue(method = "updateTargetedEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=9.0D"))
	private double pehkui$updateTargetedEntity$squaredMaxDistance(double value, float tickDelta)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getEntityReachScale(entity, tickDelta);
			
			if (scale != 1.0F)
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
}
