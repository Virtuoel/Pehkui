package virtuoel.pehkui.api;

import java.util.function.Supplier;

public class TypedScaleModifier extends ScaleModifier
{
	private final Supplier<ScaleType> type;
	
	public TypedScaleModifier(final Supplier<ScaleType> type)
	{
		this.type = type;
	}
	
	public ScaleType getType()
	{
		return type.get();
	}
	
	@Override
	public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
	{
		return type.get().getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
	}
	
	@Override
	public float modifyPrevScale(final ScaleData scaleData, float modifiedScale)
	{
		return type.get().getScaleData(scaleData.getEntity()).getPrevScale() * modifiedScale;
	}
}
