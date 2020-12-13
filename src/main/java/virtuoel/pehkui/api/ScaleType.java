package virtuoel.pehkui.api;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ScaleType
{
	@Deprecated
	public static final Map<Identifier, ScaleType> REGISTRY = ScaleRegistries.SCALE_TYPES;
	
	public static final ScaleType INVALID = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES));
	public static final ScaleType BASE = registerDimensionScale("base");
	public static final ScaleType WIDTH = registerDimensionScale("width");
	public static final ScaleType HEIGHT = registerDimensionScale("height");
	public static final ScaleType MOTION = register("motion");
	public static final ScaleType REACH = register("reach");
	public static final ScaleType DROPS = register("drops");
	public static final ScaleType PROJECTILES = register("projectiles");
	public static final ScaleType EXPLOSIONS = register("explosions");
	
	public static ScaleType register(Identifier id, ScaleType entry)
	{
		return REGISTRY.computeIfAbsent(id, i -> entry);
	}
	
	private static ScaleType register(String path)
	{
		return register(new Identifier("pehkui", path), new ScaleType());
	}
	
	private static ScaleType registerDimensionScale(String path)
	{
		return register(new Identifier("pehkui", path), new ScaleType(e -> Optional.of(e::calculateDimensions)));
	}
	
	public final Function<Entity, Optional<Runnable>> changeListenerFactory;
	
	public ScaleType()
	{
		this(e -> Optional.empty());
	}
	
	public ScaleType(Function<Entity, Optional<Runnable>> changeListenerFactory)
	{
		this.changeListenerFactory = changeListenerFactory;
	}
}
