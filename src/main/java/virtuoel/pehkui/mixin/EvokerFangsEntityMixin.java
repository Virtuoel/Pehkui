package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EvokerFangsEntity.class)
public class EvokerFangsEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;DDDFILnet/minecraft/entity/LivingEntity;)V")
	private void onConstruct(World world, double x, double y, double z, float yaw, int warmup, LivingEntity owner, CallbackInfo info)
	{
		ScaleUtils.setScaleOfProjectile((Entity) (Object) this, owner);
	}
}
