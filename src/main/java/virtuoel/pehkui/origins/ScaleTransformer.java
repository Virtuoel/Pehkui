package virtuoel.pehkui.origins;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleOperation;
import virtuoel.pehkui.api.ScaleType;

public class ScaleTransformer
{

	private final ScaleType scaleType;
	private final float value;
	private final ScaleOperation operation;
	private final boolean persist;
	private final int delay;
	private final Float2FloatFunction easing;

	public ScaleTransformer(ScaleType scaleType, float value, ScaleOperation operation, boolean persist, int delay, Float2FloatFunction easing)
	{
		this.scaleType = scaleType;
		this.value = value;
		this.operation = operation;
		this.persist = persist;
		this.delay = delay;
		this.easing = easing;
	}

	public void applyTo(Entity entity)
	{
		ScaleData data = scaleType.getScaleData(entity);
		data.setScaleTickDelay(delay);
		data.setEasing(easing);
		data.setTargetScale(operation.calculate(data.getTargetScale(), value));
	}

	public void reset(Entity entity)
	{
		ScaleData data = scaleType.getScaleData(entity);
		data.setScaleTickDelay(delay);
		data.setEasing(easing);
		data.setTargetScale(1.0F);
	}

	public ScaleType getScaleType()
	{
		return scaleType;
	}

	public float getValue()
	{
		return value;
	}

	public ScaleOperation getOperation()
	{
		return operation;
	}

	public boolean shouldPersist()
	{
		return persist;
	}

	public int getDelay()
	{
		return delay;
	}

	public Float2FloatFunction getEasing()
	{
		return easing;
	}
}
