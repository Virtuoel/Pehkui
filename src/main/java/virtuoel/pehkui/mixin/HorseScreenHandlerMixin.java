package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.HorseScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin
{
	@Unique private static final ThreadLocal<Float> REACH_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	
	@Inject(method = "canUse", at = @At("HEAD"))
	private void onCanUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info)
	{
		REACH_SCALE.set(ScaleUtils.getReachScale(player));
	}
	
	@ModifyConstant(method = "canUse", constant = @Constant(floatValue = 8.0F))
	private float canUseModifyDistance(float value)
	{
		final float scale = REACH_SCALE.get();
		
		return scale != 1.0F ? scale * value : value;
	}
}
