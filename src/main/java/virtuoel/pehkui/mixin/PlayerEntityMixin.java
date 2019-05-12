package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@Inject(at = @At("RETURN"), method = "getSize", cancellable = true)
	private void onGetSize(EntityPose entityPose_1, CallbackInfoReturnable<EntitySize> info)
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
}
