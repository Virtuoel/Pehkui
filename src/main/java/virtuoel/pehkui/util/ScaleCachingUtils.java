package virtuoel.pehkui.util;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class ScaleCachingUtils
{
	public static final boolean ENABLE_CACHING = true;
	
	public static final ScaleType[] CACHED = {
		ScaleTypes.INTERACTION_BOX_WIDTH,
		ScaleTypes.INTERACTION_BOX_HEIGHT,
		ScaleTypes.MOTION,
		ScaleTypes.BASE,
		ScaleTypes.HEIGHT,
		ScaleTypes.EYE_HEIGHT,
		ScaleTypes.STEP_HEIGHT,
		ScaleTypes.HITBOX_WIDTH,
		ScaleTypes.WIDTH,
	};
	
	public static ScaleData getCachedData(final ScaleData[] cache, final ScaleType type)
	{
		for (int i = 0; i < CACHED.length; i++)
		{
			if (type == CACHED[i])
			{
				return cache[i];
			}
		}
		
		return null;
	}
	
	public static void setCachedData(final ScaleData[] cache, final ScaleType type, final ScaleData data)
	{
		for (int i = 0; i < CACHED.length; i++)
		{
			if (type == CACHED[i])
			{
				cache[i] = data;
			}
		}
	}
}
