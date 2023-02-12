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
	private final boolean persistent;
	private final int delay;
	private final Float2FloatFunction easing;

	public ScaleTransformer(ScaleType scaleType, float value, ScaleOperation operation, boolean persistent, int delay, Float2FloatFunction easing)
	{
		this.scaleType = scaleType;
		this.value = value;
		this.operation = operation;
		this.persistent = persistent;
		this.delay = delay;
		this.easing = easing;
	}

	public void applyTo(Entity entity)
	{
		if (entity.world.isClient)
			return;
		ScaleData data = scaleType.getScaleData(entity);
		data.setScaleTickDelay(delay);
		data.setEasing(easing);
		data.setPersistence(persistent);
		data.setTargetScale(operation.calculate(data.getTargetScale(), value));
	}

	public void reset(Entity entity)
	{
		if (entity.world.isClient)
			return;
		ScaleData data = scaleType.getScaleData(entity);
		data.setScaleTickDelay(delay);
		data.setEasing(easing);
		data.setPersistence(persistent);
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
		return persistent;
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
