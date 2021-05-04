package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(OtherClientPlayerEntity.class)
public class OtherClientPlayerEntityMixin
{
	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 4.0F))
	private float modifyLimbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
}
