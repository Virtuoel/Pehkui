package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.HorseScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin
{
	@Unique private static final ThreadLocal<Float> pehkui$REACH_SCALE = ThreadLocal.withInitial(() -> 1.0F);
	
	@Inject(method = "canUse", at = @At("HEAD"))
	private void pehkui$canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info)
	{
		pehkui$REACH_SCALE.set(ScaleUtils.getEntityReachScale(player));
	}
	
	@ModifyExpressionValue(method = "canUse", at = @At(value = "CONSTANT", args = "floatValue=8.0F"))
	private float pehkui$canUse$distance(float value)
	{
		final float scale = pehkui$REACH_SCALE.get();
		
		return scale != 1.0F ? scale * value : value;
	}
}
