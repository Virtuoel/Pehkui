package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AbstractHorseEntity.class)
public class HorseBaseEntityMixin
{
	@ModifyConstant(method = MixinConstants.TRAVEL, constant = @Constant(floatValue = 4.0F))
	private float pehkui$travel$limbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
}
