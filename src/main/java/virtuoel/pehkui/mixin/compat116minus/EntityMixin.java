package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@ModifyArg(method = "method_5623(DZLnet/minecraft/class_2680;Lnet/minecraft/class_2338;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2248;method_9554(Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Lnet/minecraft/class_1297;F)V", remap = false), remap = false)
	private float onFallModifyFallDistance(float distance)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return distance / scale;
			}
		}
		
		return distance;
	}
}
