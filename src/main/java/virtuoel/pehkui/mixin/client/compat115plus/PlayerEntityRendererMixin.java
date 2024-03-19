package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
{
	@ModifyReturnValue(method = "getPositionOffset", at = @At("RETURN"))
	private Vec3d pehkui$getPositionOffset(Vec3d original, AbstractClientPlayerEntity entity, float tickDelta)
	{
		if (original != Vec3d.ZERO)
		{
			return original.multiply(ScaleUtils.getModelHeightScale(entity, tickDelta));
		}
		
		return original;
	}
}
