package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.decoration.ArmorStandEntity;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends EntityMixin
{
	@ModifyArg(method = "getDimensions", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityDimensions;scaled(F)Lnet/minecraft/entity/EntityDimensions;"))
	private float onGetDimensionsModifyScale(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		return scale != 1.0F ? value * scale : value;
	}
}
