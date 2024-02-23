package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin
{
	@Dynamic @Shadow
	PlayerEntity field_7177; // UNMAPPED_FIELD
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.REMOVE_IF_INVALID, at = @At(value = "CONSTANT", args = "doubleValue=1024.0D"))
	private double pehkui$removeIfInvalid$distance(double value)
	{
		final float scale = ScaleUtils.getProjectileScale(field_7177);
		
		if (scale != 1.0F)
		{
			return value * scale * scale;
		}
		
		return value;
	}
}
