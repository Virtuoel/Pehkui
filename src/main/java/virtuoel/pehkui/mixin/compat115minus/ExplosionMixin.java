package virtuoel.pehkui.mixin.compat115minus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import virtuoel.pehkui.api.ScaleData;

@Mixin(Explosion.class)
public abstract class ExplosionMixin
{
	@Shadow(remap = false)
	@Final
	@Mutable
	float field_9190;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;DDDFZLnet/minecraft/class_1927$class_4179;)V", remap = false)
	private void onConstruct(World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType blockDestructionType, CallbackInfo info)
	{
		if (entity != null)
		{
			final float scale = ScaleData.of(entity).getScale();
			
			if (scale != 1.0F)
			{
				this.field_9190 *= scale;
			}
		}
	}
}
