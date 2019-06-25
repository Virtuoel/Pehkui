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
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose entityPose_1, CallbackInfoReturnable<EntityDimensions> info)
	{
		info.setReturnValue(info.getReturnValue().scaled(pehkui$getScale()));
	}
	
	@Redirect(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setPickupDelay(I)V"))
	public void onDropItemSetPickupDelayProxy(ItemEntity obj, int int_1)
	{
		final float scale = pehkui$getScale();
		if(scale != 1.0F)
		{
			((ResizableEntity) obj).setScale(scale);
			((ResizableEntity) obj).setTargetScale(scale);
		}
		obj.setPickupDelay(int_1);
	}
	
	@Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	public Box onTickMovementExpandProxy(Box obj, double double_1, double double_2, double double_3)
	{
		final float scale = pehkui$getScale();
		return obj.expand(double_1 * scale, double_2 * scale, double_3 * scale);
	}
	
	@Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	public Box onAttackProxy(Box obj, double double_1, double double_2, double double_3)
	{
		final float scale = pehkui$getScale();
		return obj.expand(double_1 * scale, double_2 * scale, double_3 * scale);
	}
}
