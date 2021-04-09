package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin
{
	@Shadow(remap = false)
	PlayerEntity field_7177; // UNMAPPED_FIELD
	
	@ModifyConstant(method = MixinConstants.REMOVE_IF_INVALID, constant = @Constant(doubleValue = 1024.0D), remap = false)
	private double removeIfInvalidModifyDistance(double value)
	{
		final float scale = ScaleUtils.getProjectileScale(field_7177);
		
		if (scale != 1.0F)
		{
			return value * scale * scale;
		}
		
		return value;
	}
}
