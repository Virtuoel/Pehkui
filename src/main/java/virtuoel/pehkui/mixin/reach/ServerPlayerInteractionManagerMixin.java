package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	/*
	@Shadow ServerPlayerEntity player;
	
	@Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeInstance;getValue()D"))
	private double processBlockBreakingActionModifyMultiplier(EntityAttributeInstance reach)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		if (scale != 1.0F)
		{
			return reach.getValue() * scale;
		}
		
		return reach.getValue();
	}
	*/
}
