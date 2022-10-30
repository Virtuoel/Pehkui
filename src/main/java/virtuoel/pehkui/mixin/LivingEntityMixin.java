package virtuoel.pehkui.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.MulticonnectCompatibility;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyArg(method = "getEyeHeight", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F"))
	private EntityDimensions pehkui$getEyeHeight$dimensions(EntityDimensions dimensions)
	{
		return dimensions.scaled(1.0F / ScaleUtils.getEyeHeightScale((Entity) (Object) this));
	}
	
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 1.0F, ordinal = 0))
	private float pehkui$travel$fallDistance(float value)
	{
		final float scale = ScaleUtils.getFallingScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return value / scale;
			}
		}
		
		return value;
	}
	
	@Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
	private void pehkui$getEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> info)
	{
		if (pose != EntityPose.SLEEPING)
		{
			final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
			
			if (scale != 1.0F)
			{
				info.setReturnValue(info.getReturnValueF() * scale);
			}
		}
	}
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void pehkui$tickMovement$minVelocity(CallbackInfo info, Vec3d velocity)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		final float scale = ScaleUtils.getMotionScale(self);
		
		if (scale < 1.0F)
		{
			final double min = scale * MulticonnectCompatibility.INSTANCE.getProtocolDependantValue(ver -> ver <= 47, 0.005D, 0.003D);
			
			double vX = velocity.x;
			double vY = velocity.y;
			double vZ = velocity.z;
			
			if (Math.abs(vX) < min)
			{
				vX = 0.0D;
			}
			
			if (Math.abs(vY) < min)
			{
				vY = 0.0D;
			}
			
			if (Math.abs(vZ) < min)
			{
				vZ = 0.0D;
			}
			
			self.setVelocity(vX, vY, vZ);
		}
	}
	
	@ModifyVariable(method = "applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At("HEAD"), argsOnly = true)
	private float pehkui$applyArmorToDamage(float value, DamageSource source, float amount)
	{
		final Entity attacker = source.getAttacker();
		final float attackScale = attacker == null ? 1.0F : ScaleUtils.getAttackScale(attacker);
		final float defenseScale = ScaleUtils.getDefenseScale((Entity) (Object) this);
		
		if (attackScale != 1.0F || defenseScale != 1.0F)
		{
			value = attackScale * value / defenseScale;
		}
		
		return value;
	}
	
	@Inject(method = "getMaxHealth", at = @At("RETURN"), cancellable = true)
	private void pehkui$getMaxHealth(CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getHealthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
	
	@Inject(method = "getAttackDistanceScalingFactor", at = @At("RETURN"), cancellable = true)
	private void pehkui$getAttackDistanceScalingFactor(@Nullable Entity entity, CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getVisibilityScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueD() * scale);
		}
	}
	
	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	private void pehkui$getJumpVelocity(CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getJumpHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
	
	@Inject(method = "applyClimbingSpeed(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "RETURN"), cancellable = true)
	private void pehkui$applyClimbingSpeed(Vec3d motion, CallbackInfoReturnable<Vec3d> info)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		if (!self.isClimbing())
		{
			return;
		}
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(self);
		
		if (width > 1.0F)
		{
			final Box bounds = self.getBoundingBox();
			
			final double halfUnscaledXLength = (bounds.getXLength() / width) / 2.0D;
			final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
			final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);
			
			final int minY = MathHelper.floor(bounds.minY);
			
			final double halfUnscaledZLength = (bounds.getZLength() / width) / 2.0D;
			final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
			final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);
			
			for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ))
			{
				if (self.world.getBlockState(pos).isScaffolding(self))
				{
					final Vec3d prev = info.getReturnValue();
					info.setReturnValue(new Vec3d(prev.x, Math.max(self.getVelocity().y, -0.15D), prev.z));
					break;
				}
			}
		}
	}
	
	@Inject(method = "isClimbing()Z", at = @At(value = "RETURN"), cancellable = true)
	private void pehkui$isClimbing(CallbackInfoReturnable<Boolean> info)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		if (info.getReturnValueZ() || self.isSpectator())
		{
			return;
		}
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(self);
		
		if (width > 1.0F)
		{
			final Box bounds = self.getBoundingBox();
			
			final double halfUnscaledXLength = (bounds.getXLength() / width) / 2.0D;
			final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
			final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);
			
			final int minY = MathHelper.floor(bounds.minY);
			
			final double halfUnscaledZLength = (bounds.getZLength() / width) / 2.0D;
			final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
			final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);
			
			for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ))
			{
				if (self.world.getBlockState(pos).isLadder(self.world, pos, self))
				{
					info.setReturnValue(true);
					break;
				}
			}
		}
	}
	
	@Redirect(method = "tickCramming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$tickCramming$getBoundingBox(LivingEntity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
}
