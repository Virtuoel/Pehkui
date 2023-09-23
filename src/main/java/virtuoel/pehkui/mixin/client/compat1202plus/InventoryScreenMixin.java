package virtuoel.pehkui.mixin.client.compat1202plus;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin
{
	@Unique private static final ThreadLocal<Map<ScaleType, ScaleData>> pehkui$SCALES = ThreadLocal.withInitial(Object2ObjectLinkedOpenHashMap::new);
	@Unique private static final ScaleData pehkui$IDENTITY = ScaleData.Builder.create().build();
	@Unique private static final ThreadLocal<Box> pehkui$BOX = new ThreadLocal<>();
	
	@Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;FFILorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "HEAD"))
	private static void pehkui$drawEntity$head(@Coerce Object drawContext, float x, float y, int size, @Coerce Object offset, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info)
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
		
		pehkui$BOX.set(entity.getBoundingBox());
		
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
	
	@Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;FFILorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "RETURN"))
	private static void pehkui$drawEntity$return(@Coerce Object drawContext, float x, float y, int size, @Coerce Object offset, @Coerce Object quaternionf, @Nullable @Coerce Object quaternionf2, LivingEntity entity, CallbackInfo info)
	{
		final Map<ScaleType, ScaleData> scales = pehkui$SCALES.get();
		
		for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			type.getScaleData(entity).fromScale(scales.get(type), false);
		}
		
		entity.setBoundingBox(pehkui$BOX.get());
	}
	
	@Redirect(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getHeight()F"))
	private static float pehkui$drawEntity$getHeight(LivingEntity obj)
	{
		final float value = obj.getHeight();
		final float scale = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		return scale != 1.0F ? ScaleUtils.divideClamped(value, scale) : value;
	}
}
