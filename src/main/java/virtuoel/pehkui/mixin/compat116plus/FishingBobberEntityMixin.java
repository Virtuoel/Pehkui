package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin
{
	@Shadow
	abstract PlayerEntity getPlayerOwner();
	
	@ModifyExpressionValue(method = "removeIfInvalid", at = @At(value = "CONSTANT", args = "doubleValue=1024.0D"))
	private double pehkui$removeIfInvalid$distance(double value)
	{
		final float scale = ScaleUtils.getProjectileScale(getPlayerOwner());
		
		if (scale != 1.0F)
		{
			return value * scale * scale;
		}
		
		return value;
	}
}
