package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
	private float onFallModifyFallDistance(float distance)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return distance / scale;
			}
		}
		
		return distance;
	}
}
