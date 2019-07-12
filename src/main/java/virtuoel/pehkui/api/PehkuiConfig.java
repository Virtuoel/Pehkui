package virtuoel.pehkui.api;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Lazy;

public class PehkuiConfig
{
	public static final String NAMESPACE = "pehkui";
	
	public static final Supplier<JsonObject> HANDLER =
		!FabricLoader.getInstance().isModLoaded(NAMESPACE) ?
		new Lazy<JsonObject>(JsonObject::new)::get :
		((Supplier<Supplier<Supplier<JsonObject>>>)() ->
		(() -> new virtuoel.pehkui.util.JsonConfigHandler(
			NAMESPACE,
			NAMESPACE + "/config.json",
			PehkuiConfig::createDefaultConfig
		))).get().get();
	
	public static final JsonObject DATA = HANDLER.get();
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		config.addProperty("playerMovedWronglyMax", 0.0625D);
		config.addProperty("vehicleMovedWronglyMax", 0.0625D);
		
		return config;
	}
}
