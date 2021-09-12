package virtuoel.pehkui.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
	@Unique private static final ThreadLocal<Map<ScaleType, ScaleData>> pehkui$SCALES = ThreadLocal.withInitial(Object2ObjectLinkedOpenHashMap::new);
	
	@Inject(method = "drawEntity", at = @At(value = "HEAD"))
	private static void onDrawEntityPre(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		final Map<ScaleType, ScaleData> scales = pehkui$SCALES.get();
		
		ScaleData data;
		ScaleData cachedData;
		for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			cachedData = scales.computeIfAbsent(type, t -> ScaleData.Builder.create().build());
			data = type.getScaleData(entity);
			cachedData.fromScale(data, false);
			data.resetScale(false);
		}
	}
	
	@Inject(method = "drawEntity", at = @At(value = "RETURN"))
	private static void onDrawEntityPost(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo info)
	{
		final Map<ScaleType, ScaleData> scales = pehkui$SCALES.get();
		
		for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			type.getScaleData(entity).fromScale(scales.get(type), false);
		}
	}
}
