package virtuoel.pehkui.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
	@Unique private static final ThreadLocal<ScaleData> SCALE = ThreadLocal.withInitial(() -> new ScaleData(Optional.empty()));
	
	@Inject(method = "drawEntity", at = @At(value = "HEAD"))
	private static void onDrawEntityPre(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		SCALE.get().fromScale(ScaleData.of(entity), false);
		ScaleData.of(entity).fromScale(ScaleData.IDENTITY, false);
	}
	
	@Inject(method = "drawEntity", at = @At(value = "RETURN"))
	private static void onDrawEntityPost(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		ScaleData.of(entity).fromScale(SCALE.get(), false);
	}
}
