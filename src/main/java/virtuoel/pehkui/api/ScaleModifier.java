package virtuoel.pehkui.api;

import net.minecraft.nbt.NbtCompound;

public class ScaleModifier implements Comparable<ScaleModifier>
{
	private final float priority;
	
	public ScaleModifier()
	{
		this(512.0F);
	}
	
	public ScaleModifier(final float priority)
	{
		this.priority = priority;
	}
	
	@Override
	public int compareTo(ScaleModifier o)
	{
		final int c = Float.compare(o.getPriority(), getPriority());
		
		return c != 0 ? c :
			ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, this)
			.compareTo(
				ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, o)
			);
	}
	
	/**
	 * The priority of this scale modifier.
	 * Higher priority modifiers are applied before lower priority ones.
	 * 
	 * @return priority of this modifier
	 */
	public float getPriority()
	{
		return this.priority;
	}
	
	public float modifyScale(final ScaleData scaleData, final float modifiedScale, final float delta)
	{
		return modifiedScale;
	}
	
	public float modifyPrevScale(final ScaleData scaleData, final float modifiedScale)
	{
		return modifiedScale;
	}
	
	public void readNbt(NbtCompound compound)
	{
		// TODO Auto-generated method stub
	}
}
