package virtuoel.pehkui.mixin.reach.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 990)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	/*
	@WrapOperation(method = "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeInstance;getValue()D"))
	private double pehkui$onPlayerInteractEntity$multiplier(EntityAttributeInstance reach, Operation<Double> original)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		if (scale != 1.0F)
		{
			return original.call(reach) * scale;
		}
		
		return original.call(reach);
	}
	*/
	
	@ModifyExpressionValue(method = "onPlayerInteractEntity", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=36.0D"))
	private double pehkui$onPlayerInteractEntity$distance(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
