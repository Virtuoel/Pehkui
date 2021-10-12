package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleTypes
{
	public static final ScaleType INVALID = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES));
	public static final ScaleType BASE = registerBaseScale("base");
	public static final ScaleType WIDTH = registerDimensionScale("width", ScaleModifiers.BASE_MULTIPLIER, ScaleModifiers.WIDTH_MULTIPLIER);
	public static final ScaleType HEIGHT = registerDimensionScale("height", ScaleModifiers.BASE_MULTIPLIER, ScaleModifiers.HEIGHT_MULTIPLIER);
	public static final ScaleType EYE_HEIGHT = registerDimensionScale("eye_height", ScaleModifiers.HEIGHT_MULTIPLIER);
	public static final ScaleType HITBOX_WIDTH = registerDimensionScale("hitbox_width", ScaleModifiers.WIDTH_MULTIPLIER);
	public static final ScaleType HITBOX_HEIGHT = registerDimensionScale("hitbox_height", ScaleModifiers.HEIGHT_MULTIPLIER);
	public static final ScaleType MODEL_WIDTH = register("model_width", ScaleModifiers.WIDTH_MULTIPLIER);
	public static final ScaleType MODEL_HEIGHT = register("model_height", ScaleModifiers.HEIGHT_MULTIPLIER);
	public static final ScaleType THIRD_PERSON = register("third_person", ScaleModifiers.HEIGHT_MULTIPLIER);
	public static final ScaleType MOTION = register("motion", ScaleModifiers.BASE_MULTIPLIER, ScaleModifiers.MOTION_MULTIPLIER, ScaleModifiers.MOTION_DIVISOR);
	public static final ScaleType FALLING = register("falling", ScaleModifiers.MOTION_DIVISOR);
	public static final ScaleType STEP_HEIGHT = register("step_height", ScaleModifiers.MOTION_MULTIPLIER);
	public static final ScaleType VIEW_BOBBING = register("view_bobbing", ScaleModifiers.MOTION_MULTIPLIER);
	public static final ScaleType VISIBILITY = register("visibility", ScaleModifiers.BASE_MULTIPLIER);
	public static final ScaleType FLIGHT = register("flight");
	public static final ScaleType REACH = register("reach", ScaleModifiers.BASE_MULTIPLIER);
	public static final ScaleType BLOCK_REACH = register("block_reach", ScaleModifiers.REACH_MULTIPLIER);
	public static final ScaleType ENTITY_REACH = register("entity_reach", ScaleModifiers.REACH_MULTIPLIER);
	public static final ScaleType KNOCKBACK = register("knockback");
	public static final ScaleType ATTACK = register("attack");
	public static final ScaleType DEFENSE = register("defense");
	public static final ScaleType HEALTH = register("health");
	public static final ScaleType DROPS = register("drops", ScaleModifiers.BASE_MULTIPLIER);
	public static final ScaleType HELD_ITEM = register("held_item");
	public static final ScaleType PROJECTILES = register("projectiles", ScaleModifiers.BASE_MULTIPLIER);
	public static final ScaleType EXPLOSIONS = register("explosions", ScaleModifiers.BASE_MULTIPLIER);
	
	private static ScaleType register(Identifier id, ScaleType.Builder builder)
	{
		return ScaleRegistries.register(
			ScaleRegistries.SCALE_TYPES,
			id,
			builder.build()
		);
	}
	
	private static ScaleType register(Identifier id)
	{
		final ScaleType.Builder builder = ScaleType.Builder.create();
		
		return register(id, builder);
	}
	
	private static ScaleType register(String path)
	{
		return register(Pehkui.id(path));
	}
	
	private static ScaleType register(String path, ScaleModifier valueModifier, ScaleModifier... dependantModifiers)
	{
		final ScaleType.Builder builder = ScaleType.Builder.create()
			.addBaseValueModifier(valueModifier);
		
		for (ScaleModifier scaleModifier : dependantModifiers)
		{
			builder.addDependentModifier(scaleModifier);
		}
		
		return register(Pehkui.id(path), builder);
	}
	
	private static ScaleType registerBaseScale(String path)
	{
		final ScaleType.Builder builder = ScaleType.Builder.create()
			.affectsDimensions()
			.addDependentModifier(ScaleModifiers.BASE_MULTIPLIER);
		
		return register(Pehkui.id(path), builder);
	}
	
	private static ScaleType registerDimensionScale(String path, ScaleModifier valueModifier, ScaleModifier... dependantModifiers)
	{
		final ScaleType.Builder builder = ScaleType.Builder.create()
			.affectsDimensions()
			.addBaseValueModifier(valueModifier);
		
		for (ScaleModifier scaleModifier : dependantModifiers)
		{
			builder.addDependentModifier(scaleModifier);
		}
		
		return register(Pehkui.id(path), builder);
	}
}
