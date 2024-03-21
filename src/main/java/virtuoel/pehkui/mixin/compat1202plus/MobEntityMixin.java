package virtuoel.pehkui.mixin.compat1202plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin
{
	@WrapOperation(method = "getAttackBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private Box pehkui$getAttackBox$expand(Box obj, double x, double y, double z, Operation<Box> original)
	{
		final float scale = ScaleUtils.getEntityReachScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			x *= scale;
			z *= scale;
		}
		
		return original.call(obj, x, y, z);
	}
}
