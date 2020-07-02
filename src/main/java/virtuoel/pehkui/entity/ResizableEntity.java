package virtuoel.pehkui.entity;

import virtuoel.pehkui.api.ScaleData;

public interface ResizableEntity
{
	default ScaleData pehkui_getScaleData()
	{
		return ScaleData.IDENTITY;
	}
}
