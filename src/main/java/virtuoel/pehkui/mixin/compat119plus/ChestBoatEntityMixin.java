package virtuoel.pehkui.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin
{
	@Inject(method = "getPassengerHorizontalOffset", at = @At("RETURN"), cancellable = true)
	private void pehkui$getPassengerHorizontalOffset(CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
}
