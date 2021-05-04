package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.FlyingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(FlyingEntity.class)
public class FlyingEntityMixin
{
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 4.0F))
	private float modifyLimbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
}
