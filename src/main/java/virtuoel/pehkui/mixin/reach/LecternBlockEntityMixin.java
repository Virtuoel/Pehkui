package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.block.entity.LecternBlockEntity$1")
public abstract class LecternBlockEntityMixin
{
	@Shadow @Final @Mutable
	LecternBlockEntity field_17391;
	
	@ModifyConstant(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private double canPlayerUseModifyXOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockXOffset(field_17391.getPos(), player);
	}
	
	@ModifyConstant(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double canPlayerUseModifyYOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockYOffset(field_17391.getPos(), player);
	}
	
	@ModifyConstant(method = "canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private double canPlayerUseModifyZOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockZOffset(field_17391.getPos(), player);
	}
}
