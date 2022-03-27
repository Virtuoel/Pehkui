package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.SET_VELOCITY, args = { double.class, double.class, double.class, float.class, float.class }), at = @At(value = "INVOKE", desc = @Desc(owner = Vec3d.class, value = MixinConstants.MULTIPLY, args = { double.class }, ret = Vec3d.class), remap = false), remap = false)
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
