package virtuoel.pehkui.util;

import java.util.function.Supplier;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;

public class ClampingScaleModifier extends ScaleModifier
{
	private final Supplier<Double> min, max;
	
	public ClampingScaleModifier(final Supplier<Double> min, final Supplier<Double> max, final float priority)
	{
		super(priority);
		this.min = min;
		this.max = max;
	}
	
	@Override
	public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
	{
		return Math.max(Math.min(modifiedScale, max.get().floatValue()), min.get().floatValue());
	}
	
	@Override
	public float modifyPrevScale(final ScaleData scaleData, float modifiedScale)
	{
		return Math.max(Math.min(modifiedScale, max.get().floatValue()), min.get().floatValue());
	}
}
