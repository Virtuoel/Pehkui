package virtuoel.pehkui;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.origins.PehkuiPowers;
import virtuoel.pehkui.command.PehkuiEntitySelectorOptions;
import virtuoel.pehkui.util.*;

@ApiStatus.Internal
public class Pehkui implements ModInitializer
{
	public static final String MOD_ID = "pehkui";
	
	public static final ILogger LOGGER = MixinService.getService().getLogger(MOD_ID);

	public Pehkui()
	{
		ScaleTypes.INVALID.getClass();
		PehkuiConfig.BUILDER.config.get();
	}
	
	@Override
	public void onInitialize()
	{
		CommandUtils.registerArgumentTypes();
		
		PehkuiEntitySelectorOptions.register();
		
		CommandUtils.registerCommands();
		
		if (ModLoaderUtils.isModLoaded("fabric-networking-api-v1"))
		{
			ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			{
				if (!server.isHost(handler.player.getGameProfile()))
				{
					ConfigSyncUtils.syncConfigs(handler);
				}
				else
				{
					ConfigSyncUtils.resetSyncedConfigs();
				}
			});
		}
		if (ModLoaderUtils.isModLoaded("origins", ">=1.8.0"))
		{
			PehkuiPowers.register();
		}
		
		GravityChangerCompatibility.INSTANCE.getClass();
		IdentityCompatibility.INSTANCE.getClass();
		ImmersivePortalsCompatibility.INSTANCE.getClass();
		MulticonnectCompatibility.INSTANCE.getClass();
		ReachEntityAttributesCompatibility.INSTANCE.getClass();
	}
	
	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + "/" + String.join("/", paths));
	}
	
	public static final Identifier SCALE_PACKET = id("scale");
	public static final Identifier CONFIG_SYNC_PACKET = id("config_sync");
	public static final Identifier DEBUG_PACKET = id("debug");
}
