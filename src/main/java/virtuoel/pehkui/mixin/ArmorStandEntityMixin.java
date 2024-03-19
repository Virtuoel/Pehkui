package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.decoration.ArmorStandEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin
{
	@ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
	private EntityDimensions pehkui$getDimensions(EntityDimensions original)
	{
		return original.scaled(ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this), ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this));
	}
	
	@ModifyVariable(method = "getSlotFromPosition", at = @At(value = "STORE"))
	private double pehkui$getSlotFromPosition(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		return scale != 1.0F ? value / scale : value;
	}
}
