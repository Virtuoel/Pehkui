package virtuoel.pehkui.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.JsonConfigHandler;

public class PehkuiConfig
{
	private static final Collection<Consumer<JsonObject>> DEFAULT_VALUES = new ArrayList<>();
	
	public static final Common COMMON = new Common();
	public static final Client CLIENT = new Client();
	public static final Server SERVER = new Server();
	
	public static class Common
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
		
		Common()
		{
			this.scaledFallDamage = booleanConfig("scaledFallDamage", true);
			this.scaledMotion = booleanConfig("scaledMotion", true);
			this.scaledReach = booleanConfig("scaledReach", true);
			this.scaledAttack = booleanConfig("scaledAttack", true);
			this.scaledDefense = booleanConfig("scaledDefense", true);
			this.scaledHealth = booleanConfig("scaledHealth", true);
			this.scaledItemDrops = booleanConfig("scaledItemDrops", true);
			this.scaledProjectiles = booleanConfig("scaledProjectiles", true);
			this.scaledExplosions = booleanConfig("scaledExplosions", true);
			this.keepAllScalesOnRespawn = booleanConfig("keepAllScalesOnRespawn", false);
			this.scalesKeptOnRespawn = stringListConfig("scalesKeptOnRespawn");
			this.accurateNetherPortals = booleanConfig("accurateNetherPortals", true);
			this.largeScaleCollisionThreshold = doubleConfig("largeScaleCollisionThreshold", 26.0D);
			this.enableDebugCommands = booleanConfig("enableDebugCommands", false);
		}
	}
	
	public static class Client
	{
		public final Supplier<Double> minimumCameraDepth;
		
		Client()
		{
			this.minimumCameraDepth = doubleConfig("minimumCameraDepth", 0.0D);
		}
	}
	
	public static class Server
	{
		Server()
		{
			
		}
	}
	
	private static final Supplier<JsonObject> HANDLER = createConfig();
	
	private static final JsonObject DATA = HANDLER.get();
	
	private static Supplier<JsonObject> createConfig()
	{
		return new JsonConfigHandler(
			Pehkui.MOD_ID,
			"config.json",
			PehkuiConfig::createDefaultConfig
		);
	}
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		for (final Consumer<JsonObject> value : DEFAULT_VALUES)
		{
			value.accept(config);
		}
		
		return config;
	}
	
	private static Supplier<Double> doubleConfig(String config, double defaultValue)
	{
		return numberConfig(config, Number::doubleValue, defaultValue);
	}
	
	private static <T extends Number> Supplier<T> numberConfig(String config, Function<Number, T> mapper, T defaultValue)
	{
		DEFAULT_VALUES.add(c -> c.addProperty(config, defaultValue));
		
		return () -> Optional.ofNullable(DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber).map(JsonPrimitive::getAsNumber)
			.map(mapper).orElse(defaultValue);
	}
	
	private static Supplier<Boolean> booleanConfig(String config, boolean defaultValue)
	{
		DEFAULT_VALUES.add(c -> c.addProperty(config, defaultValue));
		
		return () -> Optional.ofNullable(PehkuiConfig.DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue);
	}
	
	private static Supplier<List<String>> stringListConfig(String config)
	{
		return listConfig(config, JsonElement::getAsString);
	}
	
	private static <T> Supplier<List<T>> listConfig(String config, Function<JsonElement, T> mapper)
	{
		DEFAULT_VALUES.add(c -> c.add(config, new JsonArray()));
		
		return () -> Optional.ofNullable(PehkuiConfig.DATA.get(config))
			.filter(JsonElement::isJsonArray).map(JsonElement::getAsJsonArray)
			.map(JsonArray::spliterator).map(a -> StreamSupport.stream(a, false))
			.map(s -> s.map(mapper).collect(Collectors.toList()))
			.orElseGet(ArrayList::new);
	}
}
