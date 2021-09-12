package virtuoel.pehkui.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import org.jetbrains.annotations.ApiStatus;

import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.PehkuiEntityExtensions;
import virtuoel.pehkui.util.ScaleUtils;

public class ScaleType
{
	public static final ScaleType INVALID = register(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES));
	public static final ScaleType BASE = registerBaseScale("base");
	public static final ScaleType WIDTH = registerDimensionScale("width", ScaleModifier.BASE_MULTIPLIER, ScaleModifier.WIDTH_MULTIPLIER);
	public static final ScaleType HEIGHT = registerDimensionScale("height", ScaleModifier.BASE_MULTIPLIER, ScaleModifier.HEIGHT_MULTIPLIER);
	public static final ScaleType EYE_HEIGHT = registerDimensionScale("eye_height", ScaleModifier.HEIGHT_MULTIPLIER);
	public static final ScaleType HITBOX_WIDTH = registerDimensionScale("hitbox_width", ScaleModifier.WIDTH_MULTIPLIER);
	public static final ScaleType HITBOX_HEIGHT = registerDimensionScale("hitbox_height", ScaleModifier.HEIGHT_MULTIPLIER);
	public static final ScaleType MODEL_WIDTH = register("model_width", ScaleModifier.WIDTH_MULTIPLIER);
	public static final ScaleType MODEL_HEIGHT = register("model_height", ScaleModifier.HEIGHT_MULTIPLIER);
	public static final ScaleType THIRD_PERSON = register("third_person", ScaleModifier.HEIGHT_MULTIPLIER);
	public static final ScaleType MOTION = register("motion", ScaleModifier.BASE_MULTIPLIER, ScaleModifier.MOTION_MULTIPLIER, ScaleModifier.MOTION_DIVISOR);
	public static final ScaleType FALLING = register("falling", ScaleModifier.MOTION_DIVISOR);
	public static final ScaleType STEP_HEIGHT = register("step_height", ScaleModifier.MOTION_MULTIPLIER);
	public static final ScaleType VIEW_BOBBING = register("view_bobbing", ScaleModifier.MOTION_MULTIPLIER);
	public static final ScaleType FLIGHT = register("flight");
	public static final ScaleType REACH = register("reach", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType BLOCK_REACH = register("block_reach", ScaleModifier.REACH_MULTIPLIER);
	public static final ScaleType ENTITY_REACH = register("entity_reach", ScaleModifier.REACH_MULTIPLIER);
	public static final ScaleType KNOCKBACK = register("knockback");
	public static final ScaleType ATTACK = register("attack");
	public static final ScaleType DEFENSE = register("defense");
	public static final ScaleType HEALTH = register("health");
	public static final ScaleType DROPS = register("drops", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType HELD_ITEM = register("held_item");
	public static final ScaleType PROJECTILES = register("projectiles", ScaleModifier.BASE_MULTIPLIER);
	public static final ScaleType EXPLOSIONS = register("explosions", ScaleModifier.BASE_MULTIPLIER);
	
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
				type.getScaleChangedEvent().add(Builder::calculateDimensions);
			}
			
			if (!this.dependentModifiers.isEmpty())
			{
				type.getScaleChangedEvent().add(createModifiedDataSyncEvent(this.dependentModifiers));
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
				
				if (!ScaleUtils.isAboveCollisionThreshold(e))
				{
					e.calculateDimensions();
				}
				
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
	
	private final Collection<ScaleEventCallback> scaleChangedEvent = new ArrayList<>();
	
	public Collection<ScaleEventCallback> getScaleChangedEvent()
	{
		return scaleChangedEvent;
	}
	
	private final Collection<ScaleEventCallback> preTickEvent = new ArrayList<>();
	
	public Collection<ScaleEventCallback> getPreTickEvent()
	{
		return preTickEvent;
	}
	
	private final Collection<ScaleEventCallback> postTickEvent = new ArrayList<>();
	
	public Collection<ScaleEventCallback> getPostTickEvent()
	{
		return postTickEvent;
	}
	
	private static ScaleType register(Identifier id, Builder builder)
	{
		return ScaleRegistries.register(
			ScaleRegistries.SCALE_TYPES,
			id,
			builder.build()
		);
	}
	
	private static ScaleType register(Identifier id)
	{
		final Builder builder = Builder.create();
		
		return register(id, builder);
	}
	
	private static ScaleType register(String path)
	{
		return register(Pehkui.id(path));
	}
	
	private static ScaleType register(String path, ScaleModifier valueModifier, ScaleModifier... dependantModifiers)
	{
		final Builder builder = Builder.create()
			.addBaseValueModifier(valueModifier);
		
		for (ScaleModifier scaleModifier : dependantModifiers)
		{
			builder.addDependentModifier(scaleModifier);
		}
		
		return register(Pehkui.id(path), builder);
	}
	
	private static ScaleType registerBaseScale(String path)
	{
		final Builder builder = Builder.create()
			.affectsDimensions()
			.addDependentModifier(ScaleModifier.BASE_MULTIPLIER);
		
		return register(Pehkui.id(path), builder);
	}
	
	private static ScaleType registerDimensionScale(String path, ScaleModifier valueModifier, ScaleModifier... dependantModifiers)
	{
		final Builder builder = Builder.create()
			.affectsDimensions()
			.addBaseValueModifier(valueModifier);
		
		for (ScaleModifier scaleModifier : dependantModifiers)
		{
			builder.addDependentModifier(scaleModifier);
		}
		
		return register(Pehkui.id(path), builder);
	}
}
