package virtuoel.pehkui.api;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.JsonConfigHandler;

public class PehkuiConfig
{
	public static final Supplier<JsonObject> HANDLER =
		new JsonConfigHandler(
			Pehkui.MOD_ID,
			Pehkui.MOD_ID + "/config.json",
			PehkuiConfig::createDefaultConfig
		);
	
	public static final JsonObject DATA = HANDLER.get();
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		config.addProperty("scaledFallDamage", true);
		config.addProperty("scaledMotion", true);
		config.addProperty("scaledReach", true);
		config.addProperty("scaledItemDrops", true);
		config.addProperty("scaledProjectiles", true);
		config.addProperty("scaledExplosions", true);
		config.addProperty("keepScaleOnRespawn", false);
		config.addProperty("accurateNetherPortals", true);
		config.addProperty("minimumRenderingDepth", 0.001F);
		
		return config;
	}
}
