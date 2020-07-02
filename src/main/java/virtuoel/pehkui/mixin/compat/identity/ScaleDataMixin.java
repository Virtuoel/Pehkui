package virtuoel.pehkui.mixin.compat.identity;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.util.IdentityCompatibility;

@Mixin(value = ScaleData.class, remap = false)
public class ScaleDataMixin
{
	@Inject(at = @At("HEAD"), method = "of", cancellable = true, remap = false)
	private static void onOf(Entity entity, CallbackInfoReturnable<ScaleData> info)
	{
		if (entity.getType() == EntityType.PLAYER)
		{
			Optional<LivingEntity> identity = IdentityCompatibility.INSTANCE.getIdentity((PlayerEntity) entity);
			
			identity.ifPresent(e ->
			{
				info.setReturnValue(ScaleData.of(e));
			});
		}
	}
}
