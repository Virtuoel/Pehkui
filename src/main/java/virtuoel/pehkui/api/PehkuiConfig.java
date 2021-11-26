package virtuoel.pehkui.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.ClampingScaleModifier;
import virtuoel.pehkui.util.ScaleUtils;
import virtuoel.pehkui.util.VersionUtils;

public class PehkuiConfig
{
	@ApiStatus.Internal
	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	@ApiStatus.Internal
	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	@ApiStatus.Internal
	public static final ForgeConfigSpec serverSpec;
	public static final Server SERVER;
	
	@ApiStatus.Internal
	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading configEvent)
	{
		Pehkui.LOGGER.debug(
			"Loaded Pehkui config file {}", configEvent.getConfig().getFileName()
		);
	}
	
	@ApiStatus.Internal
	@SubscribeEvent
	public static void onFileChange(ModConfigEvent.Reloading configEvent)
	{
		Pehkui.LOGGER.debug(
			"Pehkui config just got changed on the file system!"
		);
	}
	
	static
	{
		Pair<?, ForgeConfigSpec> specPair;
		
		specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = (Client) specPair.getLeft();
		
		specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = (Common) specPair.getLeft();
		
		specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = (Server) specPair.getLeft();
	}
	
	public static class Client
	{
		public final ForgeConfigSpec.DoubleValue minimumCameraDepth;
		
		Client(ForgeConfigSpec.Builder builder)
		{
			builder
				.comment("Client only settings, mostly things related to rendering")
				.push("client");
			this.minimumCameraDepth = builder
				.translation("pehkui.configgui.minimumCameraDepth")
				.defineInRange("minimumCameraDepth", 0.0D, 0.0D, 0.05D);
			builder.pop();
		}
	}
	
	public static class Common
	{
		public final ForgeConfigSpec.BooleanValue scaledFallDamage;
		public final ForgeConfigSpec.BooleanValue scaledMotion;
		public final ForgeConfigSpec.BooleanValue scaledReach;
		public final ForgeConfigSpec.BooleanValue scaledAttack;
		public final ForgeConfigSpec.BooleanValue scaledDefense;
		public final ForgeConfigSpec.BooleanValue scaledHealth;
		public final ForgeConfigSpec.BooleanValue scaledItemDrops;
		public final ForgeConfigSpec.BooleanValue scaledProjectiles;
		public final ForgeConfigSpec.BooleanValue scaledExplosions;
		public final ForgeConfigSpec.BooleanValue keepAllScalesOnRespawn;
		public final ConfigValue<List<? extends String>> scalesKeptOnRespawn;
		public final ForgeConfigSpec.BooleanValue accurateNetherPortals;
		@Deprecated @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
		public final ForgeConfigSpec.DoubleValue largeScaleCollisionThreshold;
		public final ForgeConfigSpec.BooleanValue enableCommands;
		public final ForgeConfigSpec.BooleanValue enableDebugCommands;
		
		Common(ForgeConfigSpec.Builder builder)
		{
			builder
				.comment("General configuration settings")
				.push("general");
			this.scaledFallDamage = builder
				.translation("pehkui.configgui.scaledFallDamage")
				.define("scaledFallDamage", true);
			this.scaledMotion = builder
				.translation("pehkui.configgui.scaledMotion")
				.define("scaledMotion", true);
			this.scaledReach = builder
				.translation("pehkui.configgui.scaledReach")
				.define("scaledReach", true);
			this.scaledAttack = builder
				.translation("pehkui.configgui.scaledAttack")
				.define("scaledAttack", true);
			this.scaledDefense = builder
				.translation("pehkui.configgui.scaledDefense")
				.define("scaledDefense", true);
			this.scaledHealth = builder
				.translation("pehkui.configgui.scaledHealth")
				.define("scaledHealth", true);
			this.scaledItemDrops = builder
				.translation("pehkui.configgui.scaledItemDrops")
				.define("scaledItemDrops", true);
			this.scaledProjectiles = builder
				.translation("pehkui.configgui.scaledProjectiles")
				.define("scaledProjectiles", true);
			this.scaledExplosions = builder
				.translation("pehkui.configgui.scaledExplosions")
				.define("scaledExplosions", true);
			this.keepAllScalesOnRespawn = builder
				.translation("pehkui.configgui.keepAllScalesOnRespawn")
				.define("keepAllScalesOnRespawn", false);
			this.accurateNetherPortals = builder
				.translation("pehkui.configgui.accurateNetherPortals")
				.define("accurateNetherPortals", true);
			this.scalesKeptOnRespawn = builder
				.translation("pehkui.configgui.scalesKeptOnRespawn")
				.defineListAllowEmpty(
					path("scalesKeptOnRespawn"),
					ArrayList::new,
					s ->
					{
						try
						{
							final Identifier id = new Identifier(String.valueOf(s));
							final ScaleType scaleType = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, id);
							
							final Identifier defaultId = ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES);
							final ScaleType defaultType = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, defaultId);
							
							return scaleType != null && (scaleType != defaultType || id.equals(defaultId));
						}
						catch (InvalidIdentifierException e)
						{
							return false;
						}
					}
				);
			this.largeScaleCollisionThreshold = builder
				.translation("pehkui.configgui.largeScaleCollisionThreshold")
				.defineInRange("largeScaleCollisionThreshold", 26.0D, 16.0D, 128.0D);
			this.enableCommands = builder
				.translation("pehkui.configgui.enableCommands")
				.define("enableCommands", false);
			this.enableDebugCommands = builder
				.translation("pehkui.configgui.enableDebugCommands")
				.define("enableDebugCommands", false);
			
			builder.push("scale_limits");
			
			Identifier id;
			String namespace, path;
			ScaleType type;
			double defaultMax;
			ForgeConfigSpec.DoubleValue min, max;
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
					
					defaultMax = ((type == ScaleTypes.BLOCK_REACH || type == ScaleTypes.ENTITY_REACH) && VersionUtils.MINOR < 17) ? ScaleUtils.DEFAULT_MAXIMUM_REACH_BELOW_1_17 : Float.MAX_VALUE;
					
					builder.push(path);
					min = builder
						.translation("pehkui.configgui.scale_limits." + path + ".minimum")
						.defineInRange("minimum", Float.MIN_VALUE, Float.MIN_VALUE, defaultMax);
					max = builder
						.translation("pehkui.configgui.scale_limits." + path + ".maximum")
						.defineInRange("maximum", defaultMax, Float.MIN_VALUE, defaultMax);
					
					type.getDefaultBaseValueModifiers().add(
						ScaleRegistries.register(
							ScaleRegistries.SCALE_MODIFIERS,
							Pehkui.id("clamping", path),
							new ClampingScaleModifier(min::get, max::get, 0.0F)
						)
					);
					
					builder.pop();
				}
			}
			builder.pop();
			
			builder.pop();
		}
	}
	
	public static class Server
	{
		Server(ForgeConfigSpec.Builder builder)
		{
			builder
				.comment("Server configuration settings")
				.push("server");
			builder.pop();
		}
	}
	
	@SafeVarargs
	private static <E> ArrayList<E> path(final E... elements)
	{
		final ArrayList<E> list = new ArrayList<E>();
		Collections.addAll(list, elements);
		return list;
	}
}
