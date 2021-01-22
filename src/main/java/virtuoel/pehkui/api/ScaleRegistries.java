package virtuoel.pehkui.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleRegistries
{
	private static final Map<Map<Identifier, ?>, Supplier<?>> DEFAULT_ENTRIES = new LinkedHashMap<>();
	private static final Map<Map<Identifier, ?>, Identifier> DEFAULT_IDS = new LinkedHashMap<>();
	
	public static final BiMap<Identifier, ScaleType> SCALE_TYPES = create("invalid", () -> ScaleType.INVALID);
	public static final BiMap<Identifier, ScaleModifier> SCALE_MODIFIERS = create("identity", () -> ScaleModifier.IDENTITY);
	
	public static <E> E register(Map<Identifier, E> registry, Identifier id, E entry)
	{
		return registry.computeIfAbsent(id, i -> entry);
	}
	
	public static <E> E getEntry(Map<Identifier, E> registry, Identifier id)
	{
		return registry.get(id);
	}
	
	public static <E> Identifier getDefaultId(BiMap<Identifier, E> registry)
	{
		return DEFAULT_IDS.get(registry);
	}
	
	public static <E> Identifier getId(BiMap<Identifier, E> registry, E entry)
	{
		return registry.inverse().get(entry);
	}
	
	private static <E> BiMap<Identifier, E> create(String defaultPath, Supplier<E> defaultEntry)
	{
		return create(Pehkui.id(defaultPath), defaultEntry);
	}
	
	private static <E> BiMap<Identifier, E> create(Identifier defaultId, Supplier<E> defaultEntry)
	{
		final BiMap<Identifier, E> registry = HashBiMap.create();
		DEFAULT_IDS.put(registry, defaultId);
		DEFAULT_ENTRIES.put(registry, defaultEntry);
		return registry;
	}
}
