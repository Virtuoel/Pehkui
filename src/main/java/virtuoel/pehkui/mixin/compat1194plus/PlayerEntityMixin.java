package virtuoel.pehkui.mixin.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
	@ModifyReturnValue(method = "getOffGroundSpeed", at = @At(value = "RETURN", ordinal = 0))
	private float pehkui$getOffGroundSpeed(float original)
	{
		final float scale = ScaleUtils.getFlightScale((PlayerEntity) (Object) this);
		
		return scale != 1.0F ? original * scale : original;
	}
}
