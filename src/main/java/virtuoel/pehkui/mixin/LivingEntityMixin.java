package virtuoel.pehkui.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.util.MulticonnectCompatibility;
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "tickMovement",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V",
			shift = Shift.AFTER))
	private void pehkui$tickMovement$minVelocity(CallbackInfo info, @Local Vec3d velocity) {
		final LivingEntity self = (LivingEntity) (Object) this;

		final float scale = ScaleUtils.getMotionScale(self);

		if (scale < 1.0F) {
			final double min =
				scale * MulticonnectCompatibility.INSTANCE.getProtocolDependentValue(ver -> ver <= 47, 0.005D, 0.003D);

			double vX = velocity.x;
			double vY = velocity.y;
			double vZ = velocity.z;

			if (Math.abs(vX) < min) {
				vX = 0.0D;
			}

			if (Math.abs(vY) < min) {
				vY = 0.0D;
			}

			if (Math.abs(vZ) < min) {
				vZ = 0.0D;
			}

			self.setVelocity(vX, vY, vZ);
		}
	}

	@ModifyVariable(method = "applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", at = @At("HEAD"),
		argsOnly = true)
	private float pehkui$applyArmorToDamage(float value, DamageSource source, float amount) {
		final Entity attacker = source.getAttacker();
		final float attackScale = attacker == null ? 1.0F : ScaleUtils.getAttackScale(attacker);
		final float defenseScale = ScaleUtils.getDefenseScale((Entity) (Object) this);

		if (attackScale != 1.0F || defenseScale != 1.0F) {
			value = attackScale * value / defenseScale;
		}

		return value;
	}

	@ModifyReturnValue(method = "getMaxHealth", at = @At("RETURN"))
	private float pehkui$getMaxHealth(float original) {
		final float scale = ScaleUtils.getHealthScale((Entity) (Object) this);

		return scale != 1.0F ? original * scale : original;
	}

	@ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
	private double pehkui$getAttackDistanceScalingFactor(double original) {
		final float scale = ScaleUtils.getVisibilityScale((Entity) (Object) this);

		return scale != 1.0F ? original * scale : original;
	}

	@ModifyReturnValue(method = "applyClimbingSpeed(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
		at = @At("RETURN"))
	private Vec3d pehkui$applyClimbingSpeed(Vec3d original) {
		final LivingEntity self = (LivingEntity) (Object) this;

		if (!self.isClimbing()) {
			return original;
		}

		final float width = ScaleUtils.getBoundingBoxWidthScale(self);

		if (width > 1.0F) {
			final Box bounds = self.getBoundingBox();

			final double halfUnscaledXLength = (bounds.getLengthX() / width) / 2.0D;
			final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
			final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);

			final int minY = MathHelper.floor(bounds.minY);

			final double halfUnscaledZLength = (bounds.getLengthZ() / width) / 2.0D;
			final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
			final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);

			final World world = self.getWorld();

			for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ)) {
				if (((PehkuiBlockStateExtensions) world.getBlockState(
					pos)).pehkui_getBlock() instanceof ScaffoldingBlock) {
					return new Vec3d(original.x, Math.max(self.getVelocity().y, -0.15D), original.z);
				}
			}
		}

		return original;
	}

	@WrapOperation(method = "tickCramming", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/LivingEntity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$tickCramming$getBoundingBox(LivingEntity obj, Operation<Box> original) {
		final Box bounds = original.call(obj);

		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);

		if (interactionWidth != 1.0F || interactionHeight != 1.0F) {
			final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);

			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}

		return bounds;
	}
}
