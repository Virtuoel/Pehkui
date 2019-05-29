package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BoundingBox;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@Redirect(method = "onVehicleMove", at = @At(value = "INVOKE", target = "net/minecraft/util/math/BoundingBox.contract(D)Lnet/minecraft/util/math/BoundingBox;"))
	public BoundingBox onVehicleMoveContractProxy(BoundingBox obj, double double_1)
	{
		final float scale = ((ResizableEntity) player).getScale();
		return obj.contract(scale < 1.0F ? double_1 * scale : double_1);
	}
}
