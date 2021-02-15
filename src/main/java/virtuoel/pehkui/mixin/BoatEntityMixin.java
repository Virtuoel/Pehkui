package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends EntityMixin
{
	@ModifyArg(method = "tick", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandXProxy(double value)
	{
		return value * ScaleUtils.getWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tick", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandYProxy(double value)
	{
		return value * ScaleUtils.getHeightScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tick", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double onTickMovementExpandZProxy(double value)
	{
		return value * ScaleUtils.getWidthScale((Entity) (Object) this);
	}
}
