package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
	@ModifyArg(method = MixinConstants.SET_VELOCITY, at = @At(value = "INVOKE", target = MixinConstants.MULTIPLY, remap = false), remap = false)
	private double setVelocityModifyMultiply(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
