package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
	@ModifyArg(method = "method_7485(DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_243;method_1021(D)Lnet/minecraft/class_243;", remap = false), remap = false)
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
