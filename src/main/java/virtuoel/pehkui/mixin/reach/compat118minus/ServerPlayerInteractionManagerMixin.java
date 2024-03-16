package virtuoel.pehkui.mixin.reach.compat118minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	/*
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, at = @At(value = "CONSTANT", args = "doubleValue=36.0D"))
	private double pehkui$processBlockBreakingAction$distance(double value)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		return scale != 1.0F ? scale * scale * value : value;
	}
	*/
}
