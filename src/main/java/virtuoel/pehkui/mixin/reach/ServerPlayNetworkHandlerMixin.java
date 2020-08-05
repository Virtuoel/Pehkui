package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 64.0D))
	private double onPlayerInteractBlockModifyDistance(double value)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
	
	@ModifyConstant(method = "onPlayerInteractEntity", constant = @Constant(doubleValue = 36.0D))
	private double onPlayerInteractEntityModifyDistance(double value)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
