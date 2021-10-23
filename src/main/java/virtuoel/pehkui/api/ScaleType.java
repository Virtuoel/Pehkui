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
	}
	
	public ScaleData getScaleData(Entity entity)
	{
		return ((PehkuiEntityExtensions) entity).pehkui_getScaleData(this);
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
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public boolean isPersistent()
	{
		return getDefaultPersistence();
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
		
		public Builder affectsDimensions()
		{
			this.affectsDimensions = true;
			return this;
		}
		
		@Deprecated
		@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
		public Builder persistent()
		{
			this.defaultPersistence = true;
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
	
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType INVALID = ScaleTypes.INVALID;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType BASE = ScaleTypes.BASE;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType WIDTH = ScaleTypes.WIDTH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType HEIGHT = ScaleTypes.HEIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType EYE_HEIGHT = ScaleTypes.EYE_HEIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType HITBOX_WIDTH = ScaleTypes.HITBOX_WIDTH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType HITBOX_HEIGHT = ScaleTypes.HITBOX_HEIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType MODEL_WIDTH = ScaleTypes.MODEL_WIDTH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType MODEL_HEIGHT = ScaleTypes.MODEL_HEIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType THIRD_PERSON = ScaleTypes.THIRD_PERSON;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType MOTION = ScaleTypes.MOTION;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType FALLING = ScaleTypes.FALLING;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType STEP_HEIGHT = ScaleTypes.STEP_HEIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType VIEW_BOBBING = ScaleTypes.VIEW_BOBBING;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType FLIGHT = ScaleTypes.FLIGHT;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType REACH = ScaleTypes.REACH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType BLOCK_REACH = ScaleTypes.BLOCK_REACH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType ENTITY_REACH = ScaleTypes.ENTITY_REACH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType KNOCKBACK = ScaleTypes.KNOCKBACK;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType ATTACK = ScaleTypes.ATTACK;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType DEFENSE = ScaleTypes.DEFENSE;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType HEALTH = ScaleTypes.HEALTH;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType DROPS = ScaleTypes.DROPS;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType HELD_ITEM = ScaleTypes.HELD_ITEM;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType PROJECTILES = ScaleTypes.PROJECTILES;
	@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
	public static final ScaleType EXPLOSIONS = ScaleTypes.EXPLOSIONS;
}
