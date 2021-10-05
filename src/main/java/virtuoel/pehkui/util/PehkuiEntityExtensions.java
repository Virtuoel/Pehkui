package virtuoel.pehkui.util;

import java.util.Map;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface PehkuiEntityExtensions
{
	ScaleData pehkui_constructScaleData(ScaleType type);
	
	ScaleData pehkui_getScaleData(ScaleType type);
	
	Map<ScaleType, ScaleData> pehkui_getScales();
	
	boolean pehkui_getOnGround();
	
	void pehkui_setOnGround(boolean onGround);
}
