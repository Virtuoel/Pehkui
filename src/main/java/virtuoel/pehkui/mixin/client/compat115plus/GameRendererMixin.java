package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import virtuoel.pehkui.util.ImmersivePortalsCompatibility;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void pehkui$renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), tickDelta);
		
		if (scale != 1.0F)
		{
			double multiplier = ImmersivePortalsCompatibility.INSTANCE.getViewBobbingOffsetMultiplier();
			
			if (multiplier == 0.0D)
			{
				return;
			}
			
			multiplier *= scale - 1.0F;
			
			final PlayerEntity playerEntity = (PlayerEntity) client.getCameraEntity();
			final float speedLerp = -(playerEntity.horizontalSpeed + ((playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed) * tickDelta));
			final float strideLerp = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
			
			matrices.translate(multiplier * MathHelper.sin(speedLerp * (float) Math.PI) * strideLerp * 0.5F, multiplier * -Math.abs(MathHelper.cos(speedLerp * (float) Math.PI) * strideLerp), 0.0D);
		}
	}
}
