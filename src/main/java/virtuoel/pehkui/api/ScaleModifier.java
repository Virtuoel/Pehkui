package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleModifier implements Comparable<ScaleModifier>
{
	public static final ScaleModifier IDENTITY = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_MODIFIERS));
	public static final ScaleModifier BASE_MULTIPLIER = register("base_multiplier", new TypedScaleModifier(() -> ScaleType.BASE));
	
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
	
	private static ScaleModifier register(String path, ScaleModifier scaleModifier)
	{
		return register(Pehkui.id(path), scaleModifier);
	}
	
	private static ScaleModifier register(Identifier id, ScaleModifier scaleModifier)
	{
		return ScaleRegistries.register(
			ScaleRegistries.SCALE_MODIFIERS,
			id,
			scaleModifier
		);
	}
	
	private static ScaleModifier register(Identifier id)
	{
		return register(id, new ScaleModifier());
	}
}
