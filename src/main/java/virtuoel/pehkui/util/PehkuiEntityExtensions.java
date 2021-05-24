package virtuoel.pehkui.util;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface PehkuiEntityExtensions
{
	ScaleData pehkui_constructScaleData(ScaleType type);
	
	ScaleData pehkui_getScaleData(ScaleType type);
	
	boolean pehkui_getOnGround();
	
	void pehkui_setOnGround(boolean onGround);
}
