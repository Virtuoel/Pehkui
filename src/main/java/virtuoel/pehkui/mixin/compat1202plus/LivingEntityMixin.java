package virtuoel.pehkui.mixin.compat1202plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyArg(method = "getPassengerRidingPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPassengerAttachmentPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityDimensions;F)Lnet/joml/Vector3f;"))
	private float pehkui$getPassengerRidingPos$getPassengerAttachmentPos(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		return scale == 1.0F ? value : value * scale;
	}
	
	@Inject(at = @At("RETURN"), method = "getRidingOffset", cancellable = true)
	private void pehkui$getRidingOffset(CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() * scale);
		}
	}
}
