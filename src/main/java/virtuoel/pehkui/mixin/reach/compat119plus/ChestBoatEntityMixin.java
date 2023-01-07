package virtuoel.pehkui.mixin.reach.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
// import net.minecraft.entity.vehicle.ChestBoatEntity;
// import net.minecraft.entity.vehicle.VehicleInventory;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(/*Chest*/BoatEntity.class)
public abstract class ChestBoatEntityMixin // implements VehicleInventory
{
	/* // TODO 1.19
	@Inject(method = "canPlayerUse", at = @At("RETURN"), cancellable = true)
	private void pehkui$canPlayerUse(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> info)
	{
		if (!info.getReturnValueZ())
		{
			final float scale = ScaleUtils.getEntityReachScale(playerEntity);
			
			if (scale > 1.0F && !this.isRemoved() && this.getPos().isInRange(playerEntity.getPos(), 8.0 * scale))
			{
				info.setReturnValue(true);
			}
		}
	}
	*/
}
