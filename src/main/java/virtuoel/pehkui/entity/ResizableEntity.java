package virtuoel.pehkui.entity;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface ResizableEntity
{
	default ScaleData pehkui_constructScaleData(ScaleType type)
	{
		return new ScaleData(type.changeListenerFactory.apply((Entity) (Object) this));
	}
	
	default ScaleData pehkui_getScaleData(ScaleType type)
	{
		return ScaleData.IDENTITY;
	}
}
