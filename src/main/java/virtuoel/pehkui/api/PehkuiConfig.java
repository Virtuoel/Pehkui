package virtuoel.pehkui.api;

import java.util.Optional;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.JsonConfigHandler;

public class PehkuiConfig
{
	@Deprecated
	public static final Supplier<JsonObject> HANDLER = createConfig();
	
	@Deprecated
	public static final JsonObject DATA = HANDLER.get();
	
	private static Supplier<JsonObject> createConfig()
	{
		return new JsonConfigHandler(
			Pehkui.MOD_ID,
			Pehkui.MOD_ID + "/config.json",
			PehkuiConfig::createDefaultConfig
		);
	}
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		config.addProperty("scaledFallDamage", true);
		config.addProperty("scaledMotion", true);
		config.addProperty("scaledReach", true);
		config.addProperty("scaledAttack", true);
		config.addProperty("scaledDefense", true);
		config.addProperty("scaledHealth", true);
		config.addProperty("scaledItemDrops", true);
		config.addProperty("scaledProjectiles", true);
		config.addProperty("scaledExplosions", true);
		config.addProperty("keepScaleOnRespawn", false);
		config.addProperty("accurateNetherPortals", true);
		config.addProperty("minimumCameraDepth", 0.001F);
		
		return config;
	}
	
	public static final Client CLIENT = new Client();
	public static final Common COMMON = new Common();
	public static final Server SERVER = new Server();
	
	public static class Client
	{
		public final Supplier<Double> minimumCameraDepth;
		
		Client()
		{
			this.minimumCameraDepth = doubleConfig("minimumCameraDepth", 0.001D);
		}
	}
	
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
		public final Supplier<Boolean> keepScaleOnRespawn;
		public final Supplier<Boolean> accurateNetherPortals;
		
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
			this.keepScaleOnRespawn = booleanConfig("keepScaleOnRespawn", false);
			this.accurateNetherPortals = booleanConfig("accurateNetherPortals", true);
		}
	}
	
	public static class Server
	{
		Server()
		{
			
		}
	}
	
	private static Supplier<Double> doubleConfig(String config, double defaultValue)
	{
		return () -> Optional.ofNullable(PehkuiConfig.DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber).map(JsonPrimitive::getAsNumber)
			.map(Number::doubleValue)
			.orElse(defaultValue);
	}
	
	private static Supplier<Boolean> booleanConfig(String config, boolean defaultValue)
	{
		return () -> Optional.ofNullable(PehkuiConfig.DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue);
	}
}
