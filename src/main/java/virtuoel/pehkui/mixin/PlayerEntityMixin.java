package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		info.setReturnValue(info.getReturnValue().scaled(ScaleUtils.getWidthScale((Entity) (Object) this), ScaleUtils.getHeightScale((Entity) (Object) this)));
	}
	
	@ModifyArg(method = "tickMovement", index = 1, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
	private float onTickMovementMinVelocityProxy(float velocity)
	{
		return velocity * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setPickupDelay(I)V"))
	private void onDropItem(ItemStack stack, boolean spread, boolean thrown, CallbackInfoReturnable<ItemEntity> info, double y, ItemEntity entity)
	{
		final float scale = ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			final Vec3d pos = entity.getPos();
			
			entity.setPosition(pos.x, y + ((1.0F - scale) * 0.3D), pos.z);
		}
	}
	
	@ModifyArg(method = "tickMovement()V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandXProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tickMovement()V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandYProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tickMovement()V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandZProxy(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@Unique private static final ThreadLocal<Float> WIDTH_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	@Unique private static final ThreadLocal<Float> HEIGHT_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	
	@Inject(method = "attack", at = @At("HEAD"))
	private void onAttack(Entity target, CallbackInfo info)
	{
		WIDTH_SCALE.set(ScaleUtils.getWidthScale(target));
		HEIGHT_SCALE.set(ScaleUtils.getHeightScale(target));
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandXProxy(double value)
	{
		return value * WIDTH_SCALE.get();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandYProxy(double value)
	{
		return value * HEIGHT_SCALE.get();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandZProxy(double value)
	{
		return value * WIDTH_SCALE.get();
	}
}
