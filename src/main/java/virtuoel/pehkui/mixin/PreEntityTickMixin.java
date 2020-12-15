package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin({
	AbstractDecorationEntity.class,
	AbstractMinecartEntity.class,
	EndCrystalEntity.class,
	FallingBlockEntity.class,
	TntEntity.class
})
public abstract class PreEntityTickMixin extends EntityMixin
{
	@Inject(at = @At("HEAD"), method = "tick")
	private void onTickPre(CallbackInfo info)
	{
		for (final ScaleType scaleType : ScaleRegistries.SCALE_TYPES.values())
		{
			ScaleUtils.tickScale(pehkui_getScaleData(scaleType));
		}
	}
}
