package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;

public class ScaleModifier implements Comparable<ScaleModifier>
{
	public static final ScaleModifier IDENTITY = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_MODIFIERS));
	public static final ScaleModifier BASE_MULTIPLIER = register(new Identifier("pehkui", "base_multiplier"), new ScaleModifier()
	{
		@Override
		public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta)
		{
			return ScaleType.BASE.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	
	@Override
	public int compareTo(ScaleModifier o)
	{
		final int c = Float.compare(getPriority(), o.getPriority());
		
		return c != 0 ? c :
			ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, this)
			.compareTo(
				ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, o)
			);
	}
	
	/**
	 * The priority of this scale modifier.
	 * Lower priority modifiers are applied earlier.
	 * 
	 * @return priority of this modifier
	 */
	public float getPriority()
	{
		return 512.0F;
	}
	
	public float modifyScale(final ScaleData scaleData, final float modifiedScale, final float delta)
	{
		return modifiedScale;
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
