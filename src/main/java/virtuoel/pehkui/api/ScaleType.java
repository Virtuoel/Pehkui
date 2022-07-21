package virtuoel.pehkui.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import org.jetbrains.annotations.ApiStatus;

import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.PehkuiEntityExtensions;
import virtuoel.pehkui.util.ScaleUtils;

public class ScaleType
{
	/**
	 * @see {@link ScaleType.Builder}
	 */
	private ScaleType(Builder builder)
	{
		this.defaultBaseScale = builder.defaultBaseScale;
		this.defaultTickDelay = builder.defaultTickDelay;
		this.defaultBaseValueModifiers = builder.defaultBaseValueModifiers;
		this.baseScaleClampFunction = builder.baseScaleClampFunction;
		this.targetScaleClampFunction = builder.targetScaleClampFunction;
		this.defaultPersistence = builder.defaultPersistence;
		this.defaultEasing = builder.defaultEasing;
		this.affectsDimensions = builder.affectsDimensions;
	}
	
	public ScaleData getScaleData(Entity entity)
	{
		return ((PehkuiEntityExtensions) entity).pehkui_getScaleData(this);
	}
	
	private boolean affectsDimensions;
	
	@ApiStatus.Internal
	public boolean getAffectsDimensions()
	{
		return affectsDimensions;
	}
	
	private boolean defaultPersistence;
	
	public void setDefaultPersistence(boolean persistent)
	{
		this.defaultPersistence = persistent;
	}
	
	public boolean getDefaultPersistence()
	{
		return defaultPersistence;
	}

	private ScaleEasing defaultEasing;

	public void setDefaultEasing(ScaleEasing defaultEasing)
	{
		this.defaultEasing = defaultEasing;
	}

	public ScaleEasing getDefaultEasing()
	{
		return defaultEasing;
	}

	private float defaultBaseScale;
	
	public final float getDefaultBaseScale()
	{
		return defaultBaseScale;
	}
	
	private int defaultTickDelay;
	
	public final int getDefaultTickDelay()
	{
		return defaultTickDelay;
	}
	
	private final ToDoubleBiFunction<ScaleData, Double> baseScaleClampFunction;
	
	public double clampBaseScale(ScaleData data, double newScale)
	{
		return baseScaleClampFunction.applyAsDouble(data, newScale);
	}
	
	private final ToDoubleBiFunction<ScaleData, Double> targetScaleClampFunction;
	
	public double clampTargetScale(ScaleData data, double newScale)
	{
		return targetScaleClampFunction.applyAsDouble(data, newScale);
	}
	
	private final Set<ScaleModifier> defaultBaseValueModifiers;
	
	/**
	 * Returns a mutable sorted set of scale modifiers. These modifiers are applied to all scale data of this type.
	 * @return Set of scale modifiers sorted by priority
	 */
	public Set<ScaleModifier> getDefaultBaseValueModifiers()
	{
		return defaultBaseValueModifiers;
	}
	
	public static class Builder
	{
		private Set<ScaleModifier> defaultBaseValueModifiers = new ObjectRBTreeSet<>();
		private float defaultBaseScale = 1.0F;
		private int defaultTickDelay = 20;
		private float defaultMinPositiveScale = ScaleUtils.DEFAULT_MINIMUM_POSITIVE_SCALE;
		private float defaultMaxPositiveScale = ScaleUtils.DEFAULT_MAXIMUM_POSITIVE_SCALE;
		private ToDoubleBiFunction<ScaleData, Double> baseScaleClampFunction = (scaleData, newScale) ->
		{
			if (newScale > defaultMaxPositiveScale)
			{
				return defaultMaxPositiveScale;
			}
			else if (newScale < -defaultMaxPositiveScale)
			{
				return -defaultMaxPositiveScale;
			}
			else if (newScale > defaultMinPositiveScale || newScale < -defaultMinPositiveScale)
			{
				return newScale;
			}
			
			return scaleData.getTargetScale() < 0 ? -defaultMinPositiveScale : defaultMinPositiveScale;
		};
		private ToDoubleBiFunction<ScaleData, Double> targetScaleClampFunction = (scaleData, newScale) ->
		{
			if (newScale > defaultMaxPositiveScale)
			{
				return defaultMaxPositiveScale;
			}
			else if (newScale < -defaultMaxPositiveScale)
			{
				return -defaultMaxPositiveScale;
			}
			else if (newScale > defaultMinPositiveScale || newScale < -defaultMinPositiveScale)
			{
				return newScale;
			}
			
			return newScale < 0 ? -defaultMinPositiveScale : defaultMinPositiveScale;
		};
		private boolean affectsDimensions = false;
		private Set<ScaleModifier> dependentModifiers = new ObjectRBTreeSet<>();
		private boolean defaultPersistence = false;
		private ScaleEasing defaultEasing = ScaleEasings.LINEAR;
		
		public static Builder create()
		{
			return new Builder();
		}
		
		private Builder()
		{
			
		}
		
		public Builder defaultBaseScale(float defaultBaseScale)
		{
			this.defaultBaseScale = defaultBaseScale;
			return this;
		}
		
		public Builder defaultTickDelay(int defaultTickDelay)
		{
			this.defaultTickDelay = defaultTickDelay;
			return this;
		}
		
		public Builder defaultMinPositiveScale(float defaultMinPositiveScale)
		{
			this.defaultMinPositiveScale = defaultMinPositiveScale;
			return this;
		}
		
		public Builder defaultMaxPositiveScale(float defaultMaxPositiveScale)
		{
			this.defaultMaxPositiveScale = defaultMaxPositiveScale;
			return this;
		}
		
		public Builder clampedBaseScale(ToDoubleBiFunction<ScaleData, Double> baseScaleClampFunction)
		{
			this.baseScaleClampFunction = baseScaleClampFunction;
			return this;
		}
		
		public Builder clampedTargetScale(ToDoubleBiFunction<ScaleData, Double> targetScaleClampFunction)
		{
			this.targetScaleClampFunction = targetScaleClampFunction;
			return this;
		}
		
		public Builder addBaseValueModifier(ScaleModifier scaleModifier)
		{
			this.defaultBaseValueModifiers.add(scaleModifier);
			return this;
		}
		
		public Builder defaultPersistence(boolean defaultPersistence)
		{
			this.defaultPersistence = defaultPersistence;
			return this;
		}

		public Builder defaultEasing(ScaleEasing defaultEasing)
		{
			this.defaultEasing = defaultEasing;
			return this;
		}
		
		public Builder affectsDimensions()
		{
			this.affectsDimensions = true;
			return this;
		}
		
		public Builder addDependentModifier(ScaleModifier scaleModifier)
		{
			this.dependentModifiers.add(scaleModifier);
			return this;
		}
		
		public ScaleType build()
		{
			final ScaleType type = new ScaleType(this);
			
			if (this.affectsDimensions)
			{
				type.getScaleChangedEvent().register(Builder::calculateDimensions);
			}
			
			if (!this.dependentModifiers.isEmpty())
			{
				type.getScaleChangedEvent().register(createModifiedDataSyncEvent(this.dependentModifiers));
			}
			
			return type;
		}
		
		private static void calculateDimensions(ScaleData s)
		{
			final Entity e = s.getEntity();
			
			if (e != null)
			{
				final PehkuiEntityExtensions en = (PehkuiEntityExtensions) e;
				final boolean onGround = en.pehkui_getOnGround();
				
				e.calculateDimensions();
				
				en.pehkui_setOnGround(onGround);
			}
		}
		
		private static ScaleEventCallback createModifiedDataSyncEvent(final Collection<ScaleModifier> modifiers)
		{
			return s ->
			{
				final Entity e = s.getEntity();
				
				if (e != null)
				{
					ScaleData data;
					for (ScaleType scaleType : ScaleRegistries.SCALE_TYPES.values())
					{
						data = scaleType.getScaleData(e);
						
						if (!Collections.disjoint(modifiers, data.getBaseValueModifiers()))
						{
							data.markForSync(true);
						}
					}
				}
			};
		}
	}
	
	private final Event<ScaleEventCallback> scaleChangedEvent = createScaleEvent();
	
	public Event<ScaleEventCallback> getScaleChangedEvent()
	{
		return scaleChangedEvent;
	}
	
	private final Event<ScaleEventCallback> preTickEvent = createScaleEvent();
	
	public Event<ScaleEventCallback> getPreTickEvent()
	{
		return preTickEvent;
	}
	
	private final Event<ScaleEventCallback> postTickEvent = createScaleEvent();
	
	public Event<ScaleEventCallback> getPostTickEvent()
	{
		return postTickEvent;
	}
	
	private static Event<ScaleEventCallback> createScaleEvent()
	{
		return EventFactory.createArrayBacked(
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
	}
}
