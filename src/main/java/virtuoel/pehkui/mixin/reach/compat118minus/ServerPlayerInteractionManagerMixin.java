package virtuoel.pehkui.mixin.reach.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "method_14263", constant = @Constant(doubleValue = 36.0D), remap = false)
	private double processBlockBreakingActionModifyDistance(double value)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		return scale != 1.0F ? scale * scale * value : value;
	}
}
