package virtuoel.pehkui.api;

import java.util.function.DoubleBinaryOperator;
import java.util.function.Supplier;

public class TypedScaleModifier extends ScaleModifier
{
	private final Supplier<ScaleType> type;
	private final DoubleBinaryOperator operation;
	
	public TypedScaleModifier(final Supplier<ScaleType> type, final DoubleBinaryOperator operation)
	{
		this.type = type;
		this.operation = operation;
	}
	
	public TypedScaleModifier(final Supplier<ScaleType> type, final DoubleBinaryOperator operation, final float priority)
	{
		super(priority);
		this.type = type;
		this.operation = operation;
	}
	
	public TypedScaleModifier(final Supplier<ScaleType> type)
	{
		this(type, (modified, typed) -> modified * typed);
	}
	
	public TypedScaleModifier(final Supplier<ScaleType> type, final float priority)
	{
		this(type, (modified, typed) -> modified * typed, priority);
	}
	
	public ScaleType getType()
	{
		return type.get();
	}
	
	@Override
	public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
	{
		final ScaleType type = getType();
		
		return type == scaleData.getScaleType() ? modifiedScale : (float) operation.applyAsDouble(modifiedScale, type.getScaleData(scaleData.getEntity()).getScale(delta));
	}
	
	@Override
	public float modifyPrevScale(final ScaleData scaleData, float modifiedScale)
	{
		final ScaleType type = getType();
		
		return type == scaleData.getScaleType() ? modifiedScale : (float) operation.applyAsDouble(modifiedScale, type.getScaleData(scaleData.getEntity()).getPrevScale());
	}
}
