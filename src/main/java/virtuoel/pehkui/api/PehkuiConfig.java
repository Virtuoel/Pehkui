package virtuoel.pehkui.api;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.ClampingScaleModifier;
import virtuoel.pehkui.util.JsonConfigBuilder;

public class PehkuiConfig
{
	private static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		Pehkui.MOD_ID,
		"config.json"
	);
	
	public static final Common COMMON = new Common(BUILDER);
	public static final Client CLIENT = new Client(BUILDER);
	public static final Server SERVER = new Server(BUILDER);
	
	public static final class Common
	{
		public final Supplier<Boolean> scaledFallDamage;
		public final Supplier<Boolean> scaledMotion;
		public final Supplier<Boolean> scaledReach;
		public final Supplier<Boolean> scaledAttack;
		public final Supplier<Boolean> scaledDefense;
		public final Supplier<Boolean> scaledHealth;
		public final Supplier<Boolean> scaledItemDrops;
		public final Supplier<Boolean> scaledProjectiles;
		public final Supplier<Boolean> scaledExplosions;
		public final Supplier<Boolean> keepAllScalesOnRespawn;
		public final Supplier<List<String>> scalesKeptOnRespawn;
		public final Supplier<Boolean> accurateNetherPortals;
		public final Supplier<Double> largeScaleCollisionThreshold;
		public final Supplier<Boolean> enableDebugCommands;
		
		private Common(final JsonConfigBuilder builder)
		{
			this.scaledFallDamage = builder.booleanConfig("scaledFallDamage", true);
			this.scaledMotion = builder.booleanConfig("scaledMotion", true);
			this.scaledReach = builder.booleanConfig("scaledReach", true);
			this.scaledAttack = builder.booleanConfig("scaledAttack", true);
			this.scaledDefense = builder.booleanConfig("scaledDefense", true);
			this.scaledHealth = builder.booleanConfig("scaledHealth", true);
			this.scaledItemDrops = builder.booleanConfig("scaledItemDrops", true);
			this.scaledProjectiles = builder.booleanConfig("scaledProjectiles", true);
			this.scaledExplosions = builder.booleanConfig("scaledExplosions", true);
			this.keepAllScalesOnRespawn = builder.booleanConfig("keepAllScalesOnRespawn", false);
			this.scalesKeptOnRespawn = builder.stringListConfig("scalesKeptOnRespawn");
			this.accurateNetherPortals = builder.booleanConfig("accurateNetherPortals", true);
			this.largeScaleCollisionThreshold = builder.doubleConfig("largeScaleCollisionThreshold", 26.0D);
			this.enableDebugCommands = builder.booleanConfig("enableDebugCommands", false);
			
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
					
					if (type == ScaleType.INVALID)
					{
						continue;
					}
					
					path = id.getPath();
					
					min = builder.doubleConfig(path + ".minimum", Float.MIN_VALUE);
					max = builder.doubleConfig(path + ".maximum", Float.MAX_VALUE);
					
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
