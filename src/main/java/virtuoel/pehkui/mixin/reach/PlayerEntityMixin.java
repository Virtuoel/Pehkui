package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = PlayerEntity.class, priority = 990)
public abstract class PlayerEntityMixin
{
	@ModifyExpressionValue(method = "attack", require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=9.0F"))
	private double pehkui$attack$distance(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
