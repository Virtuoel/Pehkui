package virtuoel.pehkui.mixin.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.projectile.FireballEntity;
import virtuoel.pehkui.mixin.EntityMixin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends EntityMixin
{
	@ModifyArg(method = "method_7469", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8537(Lnet/minecraft/class_1297;DDDFZLnet/minecraft/class_1927$class_4179;)Lnet/minecraft/class_1927;", remap = false), remap = false)
	private float onOnCollisionCreateExplosionProxy(float power)
	{
		final float scale = ScaleUtils.getExplosionScale(this);
		
		return scale != 1.0F ? power * scale : power;
	}
}
