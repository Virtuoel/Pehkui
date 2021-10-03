package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleModifiers
{
	public static final ScaleModifier IDENTITY = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_MODIFIERS));
	public static final ScaleModifier BASE_MULTIPLIER = register("base_multiplier", new TypedScaleModifier(() -> ScaleTypes.BASE));
	public static final ScaleModifier MOTION_MULTIPLIER = register("motion_multiplier", new TypedScaleModifier(() -> ScaleTypes.MOTION));
	public static final ScaleModifier MOTION_DIVISOR = register("motion_divisor", new TypedScaleModifier(() -> ScaleTypes.MOTION, (m, t) -> m / t));
	public static final ScaleModifier WIDTH_MULTIPLIER = register("width_multiplier", new TypedScaleModifier(() -> ScaleTypes.WIDTH));
	public static final ScaleModifier HEIGHT_MULTIPLIER = register("height_multiplier", new TypedScaleModifier(() -> ScaleTypes.HEIGHT));
	public static final ScaleModifier REACH_MULTIPLIER = register("reach_multiplier", new TypedScaleModifier(() -> ScaleTypes.REACH));
	
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
