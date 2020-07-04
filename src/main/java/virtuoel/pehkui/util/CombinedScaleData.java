package virtuoel.pehkui.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import virtuoel.pehkui.api.ScaleData;

public class CombinedScaleData extends ScaleData
{
	final Supplier<ScaleData[]> otherData;
	
	public CombinedScaleData(Optional<Runnable> calculateDimensions, Supplier<ScaleData[]> otherData)
	{
		super(calculateDimensions);
		
		this.otherData = otherData;
	}
	
	protected ScaleData[] getData()
	{
		return otherData.get();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		for (final ScaleData d : getData())
		{
			d.tick();
		}
	}
	
	@Override
	public void setScale(float scale)
	{
		super.setScale(scale);
		
		for (final ScaleData d : getData())
		{
			d.setScale(scale);
		}
	}
	
	@Override
	public void setTargetScale(float targetScale)
	{
		super.setTargetScale(targetScale);
		
		for (final ScaleData d : getData())
		{
			d.setTargetScale(targetScale);
		}
	}
	
	@Override
	public void setScaleTickDelay(int ticks)
	{
		super.setScaleTickDelay(ticks);
		
		for (final ScaleData d : getData())
		{
			d.setScaleTickDelay(ticks);
		}
	}
	
	@Override
	public void markForSync()
	{
		super.markForSync();
		
		for (final ScaleData d : getData())
		{
			d.markForSync();
		}
	}
	
	@Override
	public void fromScale(ScaleData scaleData)
	{
		super.fromScale(scaleData);
		
		for (final ScaleData d : getData())
		{
			d.fromScale(scaleData);
		}
	}
	
	@Override
	public ScaleData fromScale(ScaleData scaleData, boolean calculateDimensions)
	{
		final ScaleData ret = super.fromScale(scaleData, calculateDimensions);
		
		for (final ScaleData d : getData())
		{
			d.fromScale(scaleData, calculateDimensions);
		}
		
		return ret;
	}
	
	@Override
	public int hashCode()
	{
		return 31 * Arrays.hashCode(getData()) + super.hashCode();
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (!(obj instanceof CombinedScaleData))
		{
			return false;
		}
		
		final CombinedScaleData other = (CombinedScaleData) obj;
		
		return super.equals(other) && Arrays.equals(getData(), other.getData());
	}
}
