package virtuoel.pehkui.util;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;

public class ReciprocalScaleModifier extends ScaleModifier
{
	public ReciprocalScaleModifier()
	{
		super();
	}
	
	public ReciprocalScaleModifier(final float priority)
	{
		super(priority);
	}
	
	@Override
	public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
	{
		return 1.0F / modifiedScale;
	}
	
	@Override
	public float modifyPrevScale(final ScaleData scaleData, float modifiedScale)
	{
		return 1.0F / modifiedScale;
	}
}
