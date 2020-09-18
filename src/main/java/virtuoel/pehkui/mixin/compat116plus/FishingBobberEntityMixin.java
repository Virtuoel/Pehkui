package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin
{
	@Shadow
	abstract PlayerEntity getPlayerOwner();
	
	@ModifyConstant(method = "removeIfInvalid", constant = @Constant(doubleValue = 1024.0D))
	private double removeIfInvalidModifyDistance(double value)
	{
		final float scale = ScaleUtils.getProjectileScale(getPlayerOwner());
		
		if (scale != 1.0F)
		{
			return value * scale * scale;
		}
		
		return value;
	}
}
