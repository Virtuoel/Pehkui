package virtuoel.pehkui.util;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public class CombinedScaleData extends ScaleData
{
	final Supplier<ScaleData[]> otherData;
	
	public CombinedScaleData(ScaleType scaleType, Entity entity, Supplier<ScaleData[]> otherData)
	{
		super(scaleType, entity);
		
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
			ScaleUtils.tickScale(d);
		}
	}
	
	@Override
	public void setBaseScale(float scale)
	{
		super.setBaseScale(scale);
		
		for (final ScaleData d : getData())
		{
			d.setBaseScale(scale);
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
	public void markForSync(boolean sync)
	{
		super.markForSync(sync);
		
		for (final ScaleData d : getData())
		{
			d.markForSync(sync);
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		for (final ScaleData d : getData())
		{
			d.onUpdate();
		}
	}
	
	@Override
	public ScaleData resetScale(boolean notifyListener)
	{
		final ScaleData ret = super.resetScale(notifyListener);
		
		for (final ScaleData d : getData())
		{
			d.resetScale(notifyListener);
		}
		
		return ret;
	}
	
	@Override
	public ScaleData fromScale(ScaleData scaleData, boolean notifyListener)
	{
		final ScaleData ret = super.fromScale(scaleData, notifyListener);
		
		for (final ScaleData d : getData())
		{
			d.fromScale(scaleData, notifyListener);
		}
		
		return ret;
	}
	
	@Override
	public ScaleData averagedFromScales(ScaleData scaleData, ScaleData... scales)
	{
		final ScaleData ret = super.averagedFromScales(scaleData, scales);
		
		for (final ScaleData d : getData())
		{
			d.averagedFromScales(scaleData, scales);
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
		
		return super.equals(obj) && Arrays.equals(getData(), ((CombinedScaleData) obj).getData());
	}
}
