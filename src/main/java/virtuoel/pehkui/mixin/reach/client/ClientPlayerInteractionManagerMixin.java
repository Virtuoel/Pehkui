package virtuoel.pehkui.mixin.reach.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin
{
	@Inject(at = @At("RETURN"), method = "getReachDistance", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void pehkui$getReachDistance(CallbackInfoReturnable<Float> info, float attrib)
	{
		if (info.getReturnValueF() == attrib - 0.5F)
		{
			info.setReturnValue(attrib * 0.9F);
		}
	}
}
