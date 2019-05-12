package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.PrimedTntEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin({
	AbstractDecorationEntity.class,
	AbstractMinecartEntity.class,
	EnderCrystalEntity.class,
	FallingBlockEntity.class,
	PrimedTntEntity.class
})
public abstract class PreEntityTickMixin implements ResizableEntity
{
	@Inject(at = @At("HEAD"), method = "tick")
	private void onTickPre(CallbackInfo info)
	{
		tickScale();
	}
}
