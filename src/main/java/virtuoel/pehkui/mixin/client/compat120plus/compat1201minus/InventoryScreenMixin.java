package virtuoel.pehkui.mixin.client.compat120plus.compat1201minus;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.MixinConstants;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
	@Unique private static final ThreadLocal<Map<ScaleType, ScaleData>> pehkui$SCALES = ThreadLocal.withInitial(Object2ObjectLinkedOpenHashMap::new);
	@Unique private static final ScaleData pehkui$IDENTITY = ScaleData.Builder.create().build();
	
	@Dynamic
	@Inject(method = MixinConstants.DRAW_ENTITY_NO_TRANSLATION, at = @At(value = "HEAD"))
	private static void pehkui$drawEntity$head(@Coerce Object drawContext, int x, int y, int k, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info, @Share("bounds") LocalRef<Box> bounds)
	{
		final Map<ScaleType, ScaleData> scales = pehkui$SCALES.get();
		
		ScaleData data;
		ScaleData cachedData;
		for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			cachedData = scales.computeIfAbsent(type, t -> ScaleData.Builder.create().type(t).build());
			data = type.getScaleData(entity);
			cachedData.fromScale(data, false);
			data.fromScale(pehkui$IDENTITY, false);
		}
		
		bounds.set(entity.getBoundingBox());
		
		final EntityDimensions dims = entity.getDimensions(entity.getPose());
		final Vec3d pos = entity.getPos();
		final double r = dims.width / 2.0D;
		final double h = dims.height;
		final double xPos = pos.x;
		final double yPos = pos.y;
		final double zPos = pos.z;
		final Box box = new Box(xPos - r, yPos, zPos - r, xPos + r, yPos + h, zPos + r);
		
		entity.setBoundingBox(box);
	}
	
	@Dynamic
	@Inject(method = MixinConstants.DRAW_ENTITY_NO_TRANSLATION, at = @At(value = "RETURN"))
	private static void pehkui$drawEntity$return(@Coerce Object drawContext, int i, int j, int k, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info, @Share("bounds") LocalRef<Box> bounds)
	{
		final Map<ScaleType, ScaleData> scales = pehkui$SCALES.get();
		
		for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			type.getScaleData(entity).fromScale(scales.get(type), false);
		}
		
		entity.setBoundingBox(bounds.get());
	}
}
