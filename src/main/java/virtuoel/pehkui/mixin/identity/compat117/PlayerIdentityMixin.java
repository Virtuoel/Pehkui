package virtuoel.pehkui.mixin.identity.compat117;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "draylar.identity.api.PlayerIdentity", remap = false)
public class PlayerIdentityMixin
{
	@Inject(at = @At("TAIL"), method = "updateIdentity", remap = false)
	private static void pehkui$updateIdentity(ServerPlayerEntity player, LivingEntity entity, CallbackInfoReturnable<Boolean> info)
	{
		if (entity != null)
		{
			ScaleUtils.loadScale(entity, player);
		}
	}
}
