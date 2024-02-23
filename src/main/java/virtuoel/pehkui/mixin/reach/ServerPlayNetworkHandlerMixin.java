package virtuoel.pehkui.mixin.reach;

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
	
	@ModifyExpressionValue(method = "onPlayerInteractBlock", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private double pehkui$onPlayerInteractBlock$distance(double value)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		return scale != 1.0F ? scale * scale * value : value;
	}
}
