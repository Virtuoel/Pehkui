package virtuoel.pehkui.entity;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

public interface ResizableEntity
{
	public static ScaleData getScaleData(Entity entity)
	{
		return ((ResizableEntity) entity).pehkui_getScaleData();
	}
	
	default ScaleData pehkui_getScaleData()
	{
		return ScaleData.IDENTITY;
	}
}
