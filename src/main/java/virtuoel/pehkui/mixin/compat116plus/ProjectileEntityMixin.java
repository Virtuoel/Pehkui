package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
	@ModifyArg(method = "method_26961", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private Box method_26961ModifyExpand(Box value)
	{
		final float width = ScaleUtils.getWidthScale(this);
		final float height = ScaleUtils.getHeightScale(this);
		
		if (width != 1.0F || height != 1.0F)
		{
			return value.expand(width - 1.0D, height - 1.0D, width - 1.0D);
		}
		
		return value;
	}
	
	@ModifyArg(method = "setVelocity(DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
	private double setVelocityModifyMultiply(double value)
	{
		final float scale = ScaleUtils.getMotionScale(this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
