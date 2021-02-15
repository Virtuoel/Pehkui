package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import virtuoel.pehkui.mixin.EntityMixin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends EntityMixin
{
	@ModifyArg(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
	private float onOnCollisionCreateExplosionProxy(float power)
	{
		final float scale = ScaleUtils.getExplosionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return power * scale;
		}
		
		return power;
	}
}
