package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin
{
	@Inject(at = @At("HEAD"), method = "copyFrom")
	private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info)
	{
		alive |= PehkuiConfig.COMMON.keepScaleOnRespawn.get();
		
		if (alive)
		{
			ScaleUtils.loadScale((Entity) (Object) this, oldPlayer);
		}
	}
}
