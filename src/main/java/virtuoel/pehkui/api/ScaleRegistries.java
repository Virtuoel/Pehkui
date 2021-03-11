package virtuoel.pehkui.api;

import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleRegistries
{
	private static final Map<Map<Identifier, ?>, Identifier> REGISTRY_IDS = new IdentityHashMap<>();
	private static final Map<Identifier, Supplier<?>> DEFAULT_ENTRIES = new LinkedHashMap<>();
	private static final Map<Identifier, Identifier> DEFAULT_IDS = new LinkedHashMap<>();
	
	public static final BiMap<Identifier, ScaleType> SCALE_TYPES = create("scale_types", "invalid", () -> ScaleType.INVALID);
	public static final BiMap<Identifier, ScaleModifier> SCALE_MODIFIERS = create("scale_modifiers", "identity", () -> ScaleModifier.IDENTITY);
	
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
		return DEFAULT_IDS.get(REGISTRY_IDS.get(registry));
	}
	
	public static <E> Identifier getId(BiMap<Identifier, E> registry, E entry)
	{
		return registry.inverse().get(entry);
	}
	
	private static <E> BiMap<Identifier, E> create(String id, String defaultPath, Supplier<E> defaultEntry)
	{
		return create(Pehkui.id(id), Pehkui.id(defaultPath), defaultEntry);
	}
	
	private static <E> BiMap<Identifier, E> create(Identifier id, Identifier defaultId, Supplier<E> defaultEntry)
	{
		final BiMap<Identifier, E> registry = HashBiMap.create();
		REGISTRY_IDS.put(registry, id);
		DEFAULT_IDS.put(id, defaultId);
		DEFAULT_ENTRIES.put(id, defaultEntry);
		return registry;
	}
}
