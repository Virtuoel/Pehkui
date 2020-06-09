package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.mixin.LivingEntityMixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "attack", constant = @Constant(doubleValue = 9.0D))
	private double attackModifyDistance(double value)
	{
		final float scale = pehkui_scaleData.getScale();
		return scale > 1.0F ? scale * scale * value : value;
	}
}
