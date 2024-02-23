package virtuoel.pehkui.mixin.reach.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyExpressionValue(method = "onPlayerInteractEntity", at = @At(value = "CONSTANT", args = "doubleValue=36.0D"))
	private double pehkui$onPlayerInteractEntity$distance(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
