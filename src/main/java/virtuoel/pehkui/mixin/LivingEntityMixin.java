package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.ScaffoldingBlock;
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
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = "getEyeHeight", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F"))
	private EntityDimensions onGetEyeHeightDimensionsProxy(EntityDimensions dimensions)
	{
		return dimensions.scaled(1.0F / ScaleUtils.getHeightScale((Entity) (Object) this));
	}
	
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 1.0F, ordinal = 0))
	private float travelModifyFallDistance(float value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return value * scale;
			}
		}
		
		return value;
	}
	
	@Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
	private void onGetEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> info)
	{
		if (pose != EntityPose.SLEEPING)
		{
			final float scale = ScaleUtils.getHeightScale((Entity) (Object) this);
			
			if (scale != 1.0F)
			{
				info.setReturnValue(info.getReturnValueF() * scale);
			}
		}
	}
	
	@ModifyConstant(method = "tickMovement", constant = @Constant(doubleValue = 0.003D))
	private double tickMovementModifyMinVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? scale * value : value;
	}
	
	@ModifyVariable(method = "applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At("HEAD"), argsOnly = true)
	private float onApplyArmorToDamage(float value, DamageSource source, float amount)
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
	private void onGetMaxHealth(CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getHealthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
	
	@Inject(method = "applyClimbingSpeed(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "RETURN"), cancellable = true)
	private void onApplyClimbingSpeed(Vec3d motion, CallbackInfoReturnable<Vec3d> info)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		if (!self.isClimbing())
		{
			return;
		}
		
		final float width = ScaleUtils.getWidthScale(self);
		
		if (width > 1.0F && !ScaleUtils.isAboveCollisionThreshold(self))
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
				if (((PehkuiBlockStateExtensions) self.world.getBlockState(pos)).pehkui_getBlock() instanceof ScaffoldingBlock)
				{
					final Vec3d prev = info.getReturnValue();
					info.setReturnValue(new Vec3d(prev.x, Math.max(self.getVelocity().y, -0.15D), prev.z));
					break;
				}
			}
		}
	}
}
