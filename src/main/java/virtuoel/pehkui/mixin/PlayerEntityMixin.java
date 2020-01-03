package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose entityPose_1, CallbackInfoReturnable<EntityDimensions> info)
	{
		info.setReturnValue(info.getReturnValue().scaled(pehkui_scaleData.getScale()));
	}
	
	@Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;"))
	public Vec3d onTickMovementGetVelocityProxy(PlayerEntity obj)
	{
		final float scale = pehkui_scaleData.getScale();
		return obj.getVelocity().multiply(scale);
	}
	
	@Redirect(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setPickupDelay(I)V"))
	public void onDropItemSetPickupDelayProxy(ItemEntity obj, int int_1)
	{
		final float scale = pehkui_scaleData.getScale();
		if(scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(obj);
			data.setScale(scale);
			data.setTargetScale(scale);
		}
		
		obj.setPosition(obj.getX(), obj.getY() + ((1.0F - scale) * 0.3D), obj.getZ());
		obj.setPickupDelay(int_1);
	}
	
	@Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	public Box onTickMovementExpandProxy(Box obj, double double_1, double double_2, double double_3)
	{
		final float scale = pehkui_scaleData.getScale();
		return obj.expand(double_1 * scale, double_2 * scale, double_3 * scale);
	}
	
	@Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	public Box onAttackProxy(Box obj, double double_1, double double_2, double double_3)
	{
		final float scale = pehkui_scaleData.getScale();
		return obj.expand(double_1 * scale, double_2 * scale, double_3 * scale);
	}
}
