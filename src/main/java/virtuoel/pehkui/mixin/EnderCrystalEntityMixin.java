package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.decoration.EnderCrystalEntity;

@Mixin(EnderCrystalEntity.class)
public abstract class EnderCrystalEntityMixin extends EntityMixin
{
	@ModifyArg(method = "damage", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
	public float onDamageCreateExplosionProxy(float power)
	{
		return power * pehkui_scaleData.getScale();
	}
}
