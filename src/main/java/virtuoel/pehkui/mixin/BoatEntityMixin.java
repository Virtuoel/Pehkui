package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Box;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends EntityMixin
{
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	public Box onTickExpandProxy(Box obj, double double_1, double double_2, double double_3)
	{
		final float scale = pehkui_scaleData.getScale();
		return obj.expand(double_1 * scale, double_2 * scale, double_3 * scale);
	}
}
