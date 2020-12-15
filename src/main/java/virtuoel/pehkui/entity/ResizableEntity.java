package virtuoel.pehkui.entity;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface ResizableEntity
{
	default ScaleData pehkui_constructScaleData(ScaleType type)
	{
		return ScaleData.Builder.create().type(type).entity((Entity) (Object) this).build();
	}
	
	default ScaleData pehkui_getScaleData(ScaleType type)
	{
		return ScaleData.IDENTITY;
	}
}
