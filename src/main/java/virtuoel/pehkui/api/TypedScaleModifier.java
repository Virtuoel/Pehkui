package virtuoel.pehkui.api;

import java.util.function.Supplier;

public class TypedScaleModifier extends ScaleModifier
{
	private final Supplier<ScaleType> type;
	
	public TypedScaleModifier(final Supplier<ScaleType> type)
	{
		this.type = type;
	}
	
	public TypedScaleModifier(final Supplier<ScaleType> type, final float priority)
	{
		super(priority);
		this.type = type;
	}
	
	public ScaleType getType()
	{
		return type.get();
	}
	
	@Override
	public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
	{
		final ScaleType type = getType();
		
		return type == scaleData.getScaleType() ? modifiedScale : type.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
	}
	
	@Override
	public float modifyPrevScale(final ScaleData scaleData, float modifiedScale)
	{
		final ScaleType type = getType();
		
		return type == scaleData.getScaleType() ? modifiedScale : type.getScaleData(scaleData.getEntity()).getPrevScale() * modifiedScale;
	}
}
