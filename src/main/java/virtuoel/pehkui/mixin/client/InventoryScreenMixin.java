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
import virtuoel.pehkui.api.ScaleType;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
	@Unique private static final ThreadLocal<ScaleData> SCALE = ThreadLocal.withInitial(() -> new ScaleData(Optional.empty()));
	@Unique private static final ThreadLocal<ScaleData> WIDTH_SCALE = ThreadLocal.withInitial(() -> new ScaleData(Optional.empty()));
	@Unique private static final ThreadLocal<ScaleData> HEIGHT_SCALE = ThreadLocal.withInitial(() -> new ScaleData(Optional.empty()));
	
	@Inject(method = "drawEntity", at = @At(value = "HEAD"))
	private static void onDrawEntityPre(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		SCALE.get().fromScale(ScaleData.of(entity, ScaleType.BASE), false);
		WIDTH_SCALE.get().fromScale(ScaleData.of(entity, ScaleType.WIDTH), false);
		HEIGHT_SCALE.get().fromScale(ScaleData.of(entity, ScaleType.HEIGHT), false);
		ScaleData.of(entity, ScaleType.BASE).fromScale(ScaleData.IDENTITY, false);
		ScaleData.of(entity, ScaleType.WIDTH).fromScale(ScaleData.IDENTITY, false);
		ScaleData.of(entity, ScaleType.HEIGHT).fromScale(ScaleData.IDENTITY, false);
	}
	
	@Inject(method = "drawEntity", at = @At(value = "RETURN"))
	private static void onDrawEntityPost(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		ScaleData.of(entity, ScaleType.BASE).fromScale(SCALE.get(), false);
		ScaleData.of(entity, ScaleType.WIDTH).fromScale(WIDTH_SCALE.get(), false);
		ScaleData.of(entity, ScaleType.HEIGHT).fromScale(HEIGHT_SCALE.get(), false);
	}
}
