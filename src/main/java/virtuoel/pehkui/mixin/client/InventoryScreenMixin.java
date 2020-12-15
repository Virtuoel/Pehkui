package virtuoel.pehkui.mixin.client;

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
	@Unique private static final ThreadLocal<ScaleData> SCALE = ThreadLocal.withInitial(ScaleData.Builder.create()::build);
	@Unique private static final ThreadLocal<ScaleData> WIDTH_SCALE = ThreadLocal.withInitial(ScaleData.Builder.create()::build);
	@Unique private static final ThreadLocal<ScaleData> HEIGHT_SCALE = ThreadLocal.withInitial(ScaleData.Builder.create()::build);
	
	@Inject(method = "drawEntity", at = @At(value = "HEAD"))
	private static void onDrawEntityPre(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		SCALE.get().fromScale(ScaleType.BASE.getScaleData(entity), false);
		WIDTH_SCALE.get().fromScale(ScaleType.WIDTH.getScaleData(entity), false);
		HEIGHT_SCALE.get().fromScale(ScaleType.HEIGHT.getScaleData(entity), false);
		ScaleType.BASE.getScaleData(entity).fromScale(ScaleData.IDENTITY, false);
		ScaleType.WIDTH.getScaleData(entity).fromScale(ScaleData.IDENTITY, false);
		ScaleType.HEIGHT.getScaleData(entity).fromScale(ScaleData.IDENTITY, false);
	}
	
	@Inject(method = "drawEntity", at = @At(value = "RETURN"))
	private static void onDrawEntityPost(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		ScaleType.BASE.getScaleData(entity).fromScale(SCALE.get(), false);
		ScaleType.WIDTH.getScaleData(entity).fromScale(WIDTH_SCALE.get(), false);
		ScaleType.HEIGHT.getScaleData(entity).fromScale(HEIGHT_SCALE.get(), false);
	}
}
