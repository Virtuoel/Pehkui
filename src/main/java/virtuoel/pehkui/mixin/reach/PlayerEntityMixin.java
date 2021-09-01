package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.mixin.LivingEntityMixin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "attack", constant = @Constant(doubleValue = 9.0D))
	private double attackModifyDistance(double value)
	{
		final float scale = ScaleUtils.getEntityReachScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * scale * value : value;
	}
}
