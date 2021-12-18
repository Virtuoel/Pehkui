package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	/*
	@Redirect(method = "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeInstance;getValue()D"))
	private double onPlayerInteractBlockModifyMultiplier(EntityAttributeInstance reach)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		if (scale != 1.0F)
		{
			return reach.getValue() * scale;
		}
		
		return reach.getValue();
	}
	*/
	@ModifyConstant(method = "onPlayerInteractEntity", constant = @Constant(doubleValue = 36.0D))
	private double onPlayerInteractEntityModifyDistance(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
