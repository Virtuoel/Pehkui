package virtuoel.pehkui.mixin.reach.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin
{
	@ModifyExpressionValue(method = "canPlayerUse", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private double pehkui$canPlayerUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
