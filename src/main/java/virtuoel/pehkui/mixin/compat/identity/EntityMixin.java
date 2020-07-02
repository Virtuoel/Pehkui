package virtuoel.pehkui.mixin.compat.identity;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.IdentityCompatibility;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow abstract EntityType<?> getType();
	
	@Inject(at = @At("HEAD"), method = "calculateDimensions")
	private void onCalculateDimensions(CallbackInfo info)
	{
		if (getType() == EntityType.PLAYER)
		{
			Optional<LivingEntity> identity = IdentityCompatibility.INSTANCE.getIdentity((PlayerEntity) (Object) this);
			
			identity.ifPresent(Entity::calculateDimensions);
		}
	}
}
