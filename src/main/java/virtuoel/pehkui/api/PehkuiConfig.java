package virtuoel.pehkui.api;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.Identifier;
import virtuoel.kanos_config.api.JsonConfigBuilder;
import virtuoel.kanos_config.api.MutableConfigEntry;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.ClampingScaleModifier;
import virtuoel.pehkui.util.ConfigSyncUtils;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.VersionUtils;

public class PehkuiConfig
{
	@ApiStatus.Internal
	public static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		Pehkui.MOD_ID,
		"config.json"
	)
	{
		@Override
		public <T> MutableConfigEntry<T> createConfigEntry(final String name, final Supplier<T> supplier, final Consumer<T> consumer)
		{
			return ConfigSyncUtils.createSyncedConfig(name, supplier, consumer);
		}
	};
	
	public static final Client CLIENT = new Client(BUILDER);
	public static final Common COMMON = new Common(BUILDER);
	public static final Server SERVER = new Server(BUILDER);
	
	public static final class Common
	{
		public final Supplier<Boolean> keepAllScalesOnRespawn;
		public final Supplier<List<String>> scalesKeptOnRespawn;
		
		public final Supplier<Boolean> accurateNetherPortals;
		
		public final Supplier<Boolean> enableCommands;
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
			this.keepAllScalesOnRespawn = builder.booleanConfig(synced("keepAllScalesOnRespawn", "boolean"), false);
			this.scalesKeptOnRespawn = builder.stringListConfig(synced("scalesKeptOnRespawn", "string_list"));
			
			this.accurateNetherPortals = builder.booleanConfig(synced("accurateNetherPortals", "boolean"), true);
			
			this.enableCommands = builder.booleanConfig("enableCommands", true);
			this.enableDebugCommands = builder.booleanConfig("enableDebugCommands", false);
			
			this.scaledFallDamage = builder.booleanConfig(synced("scaledFallDamage", "boolean"), true);
			this.scaledMotion = builder.booleanConfig(synced("scaledMotion", "boolean"), true);
			this.scaledReach = builder.booleanConfig(synced("scaledReach", "boolean"), true);
			this.scaledAttack = builder.booleanConfig(synced("scaledAttack", "boolean"), true);
			this.scaledDefense = builder.booleanConfig(synced("scaledDefense", "boolean"), true);
			this.scaledHealth = builder.booleanConfig(synced("scaledHealth", "boolean"), true);
			this.scaledItemDrops = builder.booleanConfig(synced("scaledItemDrops", "boolean"), true);
			this.scaledProjectiles = builder.booleanConfig(synced("scaledProjectiles", "boolean"), true);
			this.scaledExplosions = builder.booleanConfig(synced("scaledExplosions", "boolean"), true);
			
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
					
					min = builder.doubleConfig(synced(path + ".minimum", "double"), Float.MIN_VALUE);
					max = builder.doubleConfig(synced(path + ".maximum", "double"), ((type == ScaleTypes.BLOCK_REACH || type == ScaleTypes.ENTITY_REACH) && VersionUtils.MINOR < 17) ? ScaleUtils.DEFAULT_MAXIMUM_REACH_BELOW_1_17 : Float.MAX_VALUE);
					
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
	
	private static String synced(final String name, final String codecKey)
	{
		ConfigSyncUtils.setupSyncableConfig(name, codecKey);
		return name;
	}
}
