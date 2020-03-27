package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.google.gson.JsonElement;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyArg(method = "onVehicleMove", at = @At(value = "INVOKE", target = "net/minecraft/util/math/Box.contract(D)Lnet/minecraft/util/math/Box;"))
	public double onVehicleMoveContractProxy(double value)
	{
		final float scale = ScaleData.of(player).getScale();
		return scale < 1.0F ? value * scale : value;
	}
	
	@ModifyConstant(method = "onVehicleMove", constant = @Constant(doubleValue = 0.0625D, ordinal = 1))
	protected double hookOnVehicleMoveMaxVelocityDelta(double value)
	{
		return Optional.ofNullable(PehkuiConfig.DATA.get("vehicleMovedWronglyMax"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsDouble)
			.orElse(0.0625D);
	}
	
	@ModifyConstant(method = "onPlayerMove", constant = @Constant(doubleValue = 0.0625D, ordinal = 0))
	protected double hookOnPlayerMoveMaxVelocityDelta(double value)
	{
		return Optional.ofNullable(PehkuiConfig.DATA.get("playerMovedWronglyMax"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsDouble)
			.orElse(0.0625D);
	}
}
