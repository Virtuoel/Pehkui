package virtuoel.pehkui.api;

import org.jetbrains.annotations.ApiStatus;

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
	
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier IDENTITY = ScaleModifiers.IDENTITY;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier BASE_MULTIPLIER = ScaleModifiers.BASE_MULTIPLIER;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier MOTION_MULTIPLIER = ScaleModifiers.MOTION_MULTIPLIER;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier MOTION_DIVISOR = ScaleModifiers.MOTION_DIVISOR;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier WIDTH_MULTIPLIER = ScaleModifiers.WIDTH_MULTIPLIER;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier HEIGHT_MULTIPLIER = ScaleModifiers.HEIGHT_MULTIPLIER;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleModifier REACH_MULTIPLIER = ScaleModifiers.REACH_MULTIPLIER;
}
