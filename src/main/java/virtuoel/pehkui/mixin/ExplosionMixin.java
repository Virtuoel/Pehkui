package virtuoel.pehkui.mixin;

import javax.annotation.Nullable;

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
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(Explosion.class)
public abstract class ExplosionMixin
{
	@Shadow @Final @Mutable float power;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V")
	public void onConstruct(World world_1, @Nullable Entity entity_1, double double_1, double double_2, double double_3, float float_1, boolean boolean_1, Explosion.DestructionType explosion$DestructionType_1, CallbackInfo info)
	{
		if(entity_1 != null)
		{
			final float scale = ((ResizableEntity) entity_1).getScale();
			if(scale != 1.0F)
			{
				power *= scale;
			}
		}
	}
}
