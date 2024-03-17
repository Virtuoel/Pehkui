package virtuoel.pehkui.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
// import net.minecraft.entity.vehicle.ChestBoatEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(/*Chest*/BoatEntity.class) // TODO 1.19
public abstract class ChestBoatEntityMixin
{
	@ModifyReturnValue(method = "getPassengerHorizontalOffset", at = @At("RETURN"))
	private float pehkui$getPassengerHorizontalOffset(float original)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? original * scale : original;
	}
}
