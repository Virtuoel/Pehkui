package virtuoel.pehkui.mixin.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.EXPLOSIVE_PROJECTILE_ON_COLLISION, args = { HitResult.class }), index = 4, at = @At(value = "INVOKE", desc = @Desc(value = MixinConstants.CREATE_EXPLOSION, owner = World.class, args = { Entity.class, double.class, double.class, double.class, float.class, boolean.class, Explosion.DestructionType.class }, ret = Explosion.class), remap = false), remap = false)
	private float onOnCollisionCreateExplosionProxy(float power)
	{
		final float scale = ScaleUtils.getExplosionScale((Entity) (Object) this);
		
		return scale != 1.0F ? power * scale : power;
	}
}
