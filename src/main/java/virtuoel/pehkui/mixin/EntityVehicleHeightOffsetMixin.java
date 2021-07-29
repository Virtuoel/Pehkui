package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin({
	ArmorStandEntity.class,
	AbstractSkeletonEntity.class,
	EndermiteEntity.class,
	PatrolEntity.class,
	SilverfishEntity.class,
	ZombieEntity.class,
	AnimalEntity.class,
	PlayerEntity.class
})
public abstract class EntityVehicleHeightOffsetMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "getHeightOffset", cancellable = true)
	private void onGetHeightOffset(CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() * scale);
		}
	}
}
