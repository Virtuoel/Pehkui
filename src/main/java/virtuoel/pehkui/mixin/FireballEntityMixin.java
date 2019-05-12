package virtuoel.pehkui.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends EntityMixin
{
	@Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
	public Explosion onOnCollisionCreateExplosionProxy(World obj, @Nullable Entity entity_1, double double_1, double double_2, double double_3, float float_1, boolean boolean_1, Explosion.DestructionType explosion$DestructionType_1)
	{
		return obj.createExplosion(entity_1, double_1, double_2, double_3, float_1 * pehkui$getScale(), boolean_1, explosion$DestructionType_1);
	}
}
