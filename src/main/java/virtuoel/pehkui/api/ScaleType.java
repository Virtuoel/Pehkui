package virtuoel.pehkui.api;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.entity.ResizableEntity;

public class ScaleType
{
	@Deprecated
	public static final Map<Identifier, ScaleType> REGISTRY = ScaleRegistries.SCALE_TYPES;
	
	public static final ScaleType INVALID = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES));
	public static final ScaleType BASE = registerBaseScale("base");
	public static final ScaleType WIDTH = registerDimensionScale("width", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType HEIGHT = registerDimensionScale("height", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType MOTION = register("motion", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType REACH = register("reach", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType DROPS = register("drops", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType PROJECTILES = register("projectiles", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType EXPLOSIONS = register("explosions", ScaleModifier.BASE_MULTIPLIER);
	
	@Deprecated
	public static ScaleType register(Identifier id, ScaleType entry)
	{
		return ScaleRegistries.register(ScaleRegistries.SCALE_TYPES, id, entry);
	}
	
	private static ScaleType register(Identifier id, ScaleModifier... modifiers)
	{
		final Builder builder = Builder.create();
		
		for (ScaleModifier scaleModifier : modifiers)
		{
			builder.addBaseValueModifier(scaleModifier);
		}
		
		return ScaleRegistries.register(
			ScaleRegistries.SCALE_TYPES,
			id,
			builder.build()
		);
	}
	
	private static ScaleType register(String path, ScaleModifier... modifiers)
	{
		return register(new Identifier("pehkui", path), modifiers);
	}
	
	private static ScaleType registerBaseScale(String path)
	{
		final ScaleType type = registerDimensionScale(path);
		
		type.getScaleChangedEvent().register(s ->
		{
			ScaleData data;
			for (ScaleType scaleType : ScaleRegistries.SCALE_TYPES.values())
			{
				data = scaleType.getScaleData(s.getEntity());
				
				if (data.getBaseValueModifiers().contains(ScaleModifier.BASE_MULTIPLIER))
				{
					data.markForSync(true);
				}
			}
		});
		
		return type;
	}
	
	private static ScaleType registerDimensionScale(String path, ScaleModifier... modifiers)
	{
		final Builder builder = Builder.create();
		
		for (ScaleModifier scaleModifier : modifiers)
		{
			builder.addBaseValueModifier(scaleModifier);
		}
		
		final ScaleType type = ScaleRegistries.register(
			ScaleRegistries.SCALE_TYPES,
			new Identifier("pehkui", path),
			builder.build()
		);
		
		type.getScaleChangedEvent().register(s ->
		{
			final Entity e = s.getEntity();
			
			if (e != null)
			{
				e.calculateDimensions();
			}
		});
		
		return type;
	}
	
	public ScaleData getScaleData(Entity entity)
	{
		return ((ResizableEntity) entity).pehkui_getScaleData(this);
	}
	
	@Deprecated
	public final Function<Entity, Optional<Runnable>> changeListenerFactory = e ->
	{
		return Optional.of(() -> getScaleChangedEvent().invoker().onEvent(getScaleData(e)));
	};
	
	private final Set<ScaleModifier> defaultBaseValueModifiers;
	
	public Set<ScaleModifier> getDefaultBaseValueModifiers()
	{
		return defaultBaseValueModifiers;
	}
	
	private final Event<ScaleEventCallback> scaleChangedEvent = EventFactory.createArrayBacked(
		ScaleEventCallback.class,
		data -> {},
		(callbacks) -> (data) ->
		{
			for (ScaleEventCallback callback : callbacks)
			{
				callback.onEvent(data);
			}
		}
	);
	
	public Event<ScaleEventCallback> getScaleChangedEvent()
	{
		return scaleChangedEvent;
	}
	
	private final Event<ScaleEventCallback> preTickEvent = EventFactory.createArrayBacked(
		ScaleEventCallback.class,
		data -> {},
		(callbacks) -> (data) ->
		{
			for (ScaleEventCallback callback : callbacks)
			{
				callback.onEvent(data);
			}
		}
	);
	
	public Event<ScaleEventCallback> getPreTickEvent()
	{
		return preTickEvent;
	}
	
	private final Event<ScaleEventCallback> postTickEvent = EventFactory.createArrayBacked(
		ScaleEventCallback.class,
		data -> {},
		(callbacks) -> (data) ->
		{
			for (ScaleEventCallback callback : callbacks)
			{
				callback.onEvent(data);
			}
		}
	);
	
	public Event<ScaleEventCallback> getPostTickEvent()
	{
		return postTickEvent;
	}
	
	protected ScaleType(Set<ScaleModifier> defaultBaseValueModifiers)
	{
		this.defaultBaseValueModifiers = defaultBaseValueModifiers;
	}
	
	@Deprecated
	public ScaleType()
	{
		this(Collections.emptySet());
	}
	
	@Deprecated
	public ScaleType(Function<Entity, Optional<Runnable>> changeListenerFactory)
	{
		this(Collections.emptySet());
		getScaleChangedEvent().register(s -> changeListenerFactory.apply(s.getEntity()).ifPresent(Runnable::run));
	}
	
	public static class Builder
	{
		private Set<ScaleModifier> defaultBaseValueModifiers = new ObjectRBTreeSet<>();
		
		public static Builder create()
		{
			return new Builder();
		}
		
		private Builder()
		{
			
		}
		
		public Builder addBaseValueModifier(ScaleModifier scaleModifier)
		{
			defaultBaseValueModifiers.add(scaleModifier);
			return this;
		}
		
		public ScaleType build()
		{
			return new ScaleType(defaultBaseValueModifiers);
		}
	}
}
