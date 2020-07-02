package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		info.setReturnValue(info.getReturnValue().scaled(ScaleData.of((Entity) (Object) this).getScale()));
	}
	
	@ModifyArg(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;squaredHorizontalLength(Lnet/minecraft/util/math/Vec3d;)D"))
	private Vec3d onTickMovementGetVelocityProxy(Vec3d velocity)
	{
		return velocity.multiply(ScaleData.of((Entity) (Object) this).getScale());
	}
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setPickupDelay(I)V"))
	private void onDropItem(ItemStack stack, boolean spread, boolean thrown, CallbackInfoReturnable<ItemEntity> info, double y, ItemEntity entity)
	{
		final float scale = ScaleData.of((Entity) (Object) this).getScale();
		
		if (scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity);
			data.setScale(scale);
			data.setTargetScale(scale);
			data.markForSync();
			
			final Vec3d pos = entity.getPos();
			
			entity.updatePosition(pos.x, y + ((1.0F - scale) * 0.3D), pos.z);
		}
	}
	
	@ModifyArg(method = "tickMovement()V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandXProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
	
	@ModifyArg(method = "tickMovement()V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandYProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
	
	@ModifyArg(method = "tickMovement()V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandZProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandXProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandYProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
	
	@ModifyArg(method = "attack(Lnet/minecraft/entity/Entity;)V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onAttackExpandZProxy(double value)
	{
		return value * ScaleData.of((Entity) (Object) this).getScale();
	}
}
