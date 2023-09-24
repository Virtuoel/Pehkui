package virtuoel.pehkui.mixin.compat1202plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin
{
	@ModifyArg(method = "getAttackBox", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$getAttackBox$expand$x(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale((Entity) (Object) this);
		return scale == 1.0F ? value : value * scale;
	}
	
	@ModifyArg(method = "getAttackBox", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$getAttackBox$expand$z(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale((Entity) (Object) this);
		return scale == 1.0F ? value : value * scale;
	}
}
