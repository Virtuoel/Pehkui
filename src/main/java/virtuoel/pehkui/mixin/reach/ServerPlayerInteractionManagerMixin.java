package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 36.0D))
	private double processBlockBreakingActionModifyDistance(double value)
	{
		final float scale = ScaleData.of(player).getScale();
		return scale > 1.0F ? scale * scale * value : value;
	}
}
