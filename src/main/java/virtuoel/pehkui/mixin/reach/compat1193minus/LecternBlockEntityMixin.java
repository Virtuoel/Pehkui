package virtuoel.pehkui.mixin.reach.compat1193minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.block.entity.LecternBlockEntity$1")
public abstract class LecternBlockEntityMixin
{
	@Shadow @Final @Mutable
	LecternBlockEntity field_17391;
	
	@ModifyExpressionValue(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
	private double pehkui$canPlayerUse$xOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockXOffset(field_17391.getPos(), player);
	}
	
	@ModifyExpressionValue(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
	private double pehkui$canPlayerUse$yOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockYOffset(field_17391.getPos(), player);
	}
	
	@ModifyExpressionValue(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
	private double pehkui$canPlayerUse$zOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockZOffset(field_17391.getPos(), player);
	}
	
	@ModifyExpressionValue(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private double pehkui$canPlayerUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
