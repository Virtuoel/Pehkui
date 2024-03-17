package virtuoel.pehkui.mixin.reach.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin
{
	@ModifyExpressionValue(method = "canPlayerUse", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private double pehkui$canPlayerUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
