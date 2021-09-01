package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 64.0D))
	private double canPlayerUseModifyDistance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
