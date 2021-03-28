package virtuoel.pehkui.util;

import net.minecraft.predicate.NumberRange;
import virtuoel.pehkui.api.ScaleType;

public interface PehkuiEntitySelectorReaderExtensions
{
	ScaleType pehkui_getScaleType();
	void pehkui_setScaleType(final ScaleType scaleType);
	
	NumberRange.FloatRange pehkui_getScaleRange();
	void pehkui_setScaleRange(final NumberRange.FloatRange baseScaleRange);
	
	ScaleType pehkui_getComputedScaleType();
	void pehkui_setComputedScaleType(final ScaleType computedScaleType);
	
	NumberRange.FloatRange pehkui_getComputedScaleRange();
	void pehkui_setComputedScaleRange(final NumberRange.FloatRange computedScaleRange);
}
