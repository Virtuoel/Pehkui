package virtuoel.pehkui.mixin.reach.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyVariable(method = "updateTargetedEntity", ordinal = 0, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/Entity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"))
	private double updateTargetedEntitySetDistance(double value, float tickDelta)
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
	
	@ModifyVariable(method = "updateTargetedEntity", ordinal = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;"))
	private double updateTargetedEntityModifySquaredDistance(double value, float tickDelta)
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
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0D))
	private double updateTargetedEntityModifyDistance(double value, float tickDelta)
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
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6.0D))
	private double updateTargetedEntityModifyExtendedDistance(double value, float tickDelta)
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
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0D))
	private double updateTargetedEntityModifySquaredMaxDistance(double value, float tickDelta)
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
