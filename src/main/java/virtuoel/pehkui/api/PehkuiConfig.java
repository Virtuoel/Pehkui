package virtuoel.pehkui.api;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.Identifier;
import virtuoel.kanos_config.api.JsonConfigBuilder;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.ClampingScaleModifier;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.VersionUtils;

public class PehkuiConfig
{
	@ApiStatus.Internal
	public static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		Pehkui.MOD_ID,
		"config.json"
	);
	
	public static final Client CLIENT = new Client(BUILDER);
	public static final Common COMMON = new Common(BUILDER);
	public static final Server SERVER = new Server(BUILDER);
	
	public static final class Common
	{
		public final Supplier<Boolean> keepAllScalesOnRespawn;
		public final Supplier<List<String>> scalesKeptOnRespawn;
		
		public final Supplier<Boolean> accurateNetherPortals;
		
		@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
		public final Supplier<Double> largeScaleCollisionThreshold = () -> (double) ScaleUtils.DEFAULT_MAXIMUM_POSITIVE_SCALE;
		public final Supplier<Boolean> enableDebugCommands;
		
		public final Supplier<Boolean> scaledFallDamage;
		public final Supplier<Boolean> scaledMotion;
		public final Supplier<Boolean> scaledReach;
		public final Supplier<Boolean> scaledAttack;
		public final Supplier<Boolean> scaledDefense;
		public final Supplier<Boolean> scaledHealth;
		public final Supplier<Boolean> scaledItemDrops;
		public final Supplier<Boolean> scaledProjectiles;
		public final Supplier<Boolean> scaledExplosions;
		
		private Common(final JsonConfigBuilder builder)
		{
			this.keepAllScalesOnRespawn = builder.booleanConfig("keepAllScalesOnRespawn", false);
			this.scalesKeptOnRespawn = builder.stringListConfig("scalesKeptOnRespawn");
			
			this.accurateNetherPortals = builder.booleanConfig("accurateNetherPortals", true);
			
			this.enableDebugCommands = builder.booleanConfig("enableDebugCommands", false);
			
			this.scaledFallDamage = builder.booleanConfig("scaledFallDamage", true);
			this.scaledMotion = builder.booleanConfig("scaledMotion", true);
			this.scaledReach = builder.booleanConfig("scaledReach", true);
			this.scaledAttack = builder.booleanConfig("scaledAttack", true);
			this.scaledDefense = builder.booleanConfig("scaledDefense", true);
			this.scaledHealth = builder.booleanConfig("scaledHealth", true);
			this.scaledItemDrops = builder.booleanConfig("scaledItemDrops", true);
			this.scaledProjectiles = builder.booleanConfig("scaledProjectiles", true);
			this.scaledExplosions = builder.booleanConfig("scaledExplosions", true);
			
			Identifier id;
			String namespace, path;
			ScaleType type;
			Supplier<Double> min, max;
			for (final Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
			{
				id = entry.getKey();
				namespace = id.getNamespace();
				
				if (namespace.equals(Pehkui.MOD_ID))
				{
					type = entry.getValue();
					
					if (type == ScaleTypes.INVALID)
					{
						continue;
					}
					
					path = id.getPath();
					
					min = builder.doubleConfig(path + ".minimum", Float.MIN_VALUE);
					max = builder.doubleConfig(path + ".maximum", ((type == ScaleTypes.BLOCK_REACH || type == ScaleTypes.ENTITY_REACH) && VersionUtils.MINOR < 17) ? ScaleUtils.DEFAULT_MAXIMUM_REACH_BELOW_1_17 : Float.MAX_VALUE);
					
					type.getDefaultBaseValueModifiers().add(
						ScaleRegistries.register(
							ScaleRegistries.SCALE_MODIFIERS,
							Pehkui.id("clamping", path),
							new ClampingScaleModifier(min::get, max::get, 0.0F)
						)
					);
				}
			}
		}
	}
	
	public static final class Client
	{
		public final Supplier<Double> minimumCameraDepth;
		
		private Client(final JsonConfigBuilder builder)
		{
			this.minimumCameraDepth = builder.doubleConfig("minimumCameraDepth", 0.0D);
		}
	}
	
	public static final class Server
	{
		private Server(final JsonConfigBuilder builder)
		{
			
		}
	}
	
	private PehkuiConfig()
	{
		
	}
}
