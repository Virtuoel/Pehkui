package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
	@Dynamic
	@ModifyArg(method = MixinConstants.SET_VELOCITY, at = @At(value = "INVOKE", target = MixinConstants.MULTIPLY))
	private double pehkui$setVelocity$multiply(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
	
	@Dynamic
	@ModifyVariable(method = MixinConstants.ON_ENTITY_HIT, at = @At(value = "STORE"))
	private float pehkui$onEntityHit(float value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? ScaleUtils.divideClamped(value, scale) : value;
	}
}
