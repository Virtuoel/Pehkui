package virtuoel.pehkui.util;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.entity.ResizableEntity;

public class ScaleUtils
{
	public static void loadAverageScales(boolean sync, Object target, Object source, Object... sources)
	{
		ScaleData scaleData;
		for (ScaleType type : ScaleType.REGISTRY.values())
		{
			scaleData = ScaleData.of((Entity) target, type);
			
			ScaleData[] scales = new ScaleData[sources.length];
			
			for (int i = 0; i < sources.length; i++)
			{
				scales[i] = ScaleData.of((Entity) sources[i], type);
			}
			
			scaleData.averagedFromScales(ScaleData.of((Entity) source, type), scales);
			
			if (sync)
			{
				scaleData.markForSync();
			}
		}
	}
	
	public static void loadScale(Object target, Object source, boolean sync)
	{
		ScaleData scaleData;
		for (ScaleType type : ScaleType.REGISTRY.values())
		{
			scaleData = ScaleData.of((Entity) target, type);
			scaleData.fromScale(ScaleData.of((Entity) source, type));
			
			if (sync)
			{
				scaleData.markForSync();
			}
		}
	}
	
	public static void setScale(Object entity, float scale)
	{
		if (scale != 1.0F)
		{
			final ScaleData scaleData = ScaleData.of((Entity) entity, ScaleType.BASE);
			scaleData.setScale(scale);
			scaleData.setTargetScale(scale);
			scaleData.markForSync();
		}
	}
	
	public static float getWidthScale(Object entity)
	{
		return getWidthScale(entity, 1.0F);
	}
	
	public static float getWidthScale(Object entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.WIDTH, tickDelta);
	}
	
	public static float getHeightScale(Object entity)
	{
		return getHeightScale(entity, 1.0F);
	}
	
	public static float getHeightScale(Object entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.HEIGHT, tickDelta);
	}
	
	public static float getMotionScale(Object entity)
	{
		return getMotionScale(entity, 1.0F);
	}
	
	public static float getMotionScale(Object entity, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledMotion"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, ScaleType.MOTION, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getReachScale(Object entity)
	{
		return getReachScale(entity, 1.0F);
	}
	
	public static float getReachScale(Object entity, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, ScaleType.REACH, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getDropScale(Object entity)
	{
		return getDropScale(entity, 1.0F);
	}
	
	public static float getDropScale(Object entity, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledItemDrops"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, ScaleType.DROPS, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getProjectileScale(Object entity)
	{
		return getProjectileScale(entity, 1.0F);
	}
	
	public static float getProjectileScale(Object entity, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledProjectiles"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, ScaleType.PROJECTILES, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getExplosionScale(Object entity)
	{
		return getExplosionScale(entity, 1.0F);
	}
	
	public static float getExplosionScale(Object entity, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledExplosions"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, ScaleType.EXPLOSIONS, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getTypedScale(Object entity, ScaleType type, float tickDelta)
	{
		final ResizableEntity e = ((ResizableEntity) entity);
		
		final float scale = e.pehkui_getScaleData(ScaleType.BASE).getScale(tickDelta);
		
		if (type == ScaleType.BASE)
		{
			return scale;
		}
		
		return scale * e.pehkui_getScaleData(type).getScale(tickDelta);
	}
}
