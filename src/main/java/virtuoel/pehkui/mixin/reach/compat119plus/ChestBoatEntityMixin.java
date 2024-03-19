package virtuoel.pehkui.mixin.reach.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin
{
	@ModifyReturnValue(method = "canPlayerUse", at = @At("RETURN"))
	private boolean pehkui$canPlayerUse(boolean original, PlayerEntity playerEntity)
	{
		if (!original)
		{
			final float scale = ScaleUtils.getEntityReachScale(playerEntity);
			
			final ChestBoatEntity self = (ChestBoatEntity) (Object) this;
			
			if (scale > 1.0F && !self.isRemoved() && self.getPos().isInRange(playerEntity.getPos(), 8.0 * scale))
			{
				return true;
			}
		}
		
		return original;
	}
}
