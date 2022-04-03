package virtuoel.pehkui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.command.PehkuiEntitySelectorOptions;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.ScaleCommand;
import virtuoel.pehkui.util.CommandUtils;
import virtuoel.pehkui.util.ConfigSyncUtils;
import virtuoel.pehkui.util.GravityChangerCompatibility;
import virtuoel.pehkui.util.IdentityCompatibility;
import virtuoel.pehkui.util.ImmersivePortalsCompatibility;
import virtuoel.pehkui.util.ModLoaderUtils;
import virtuoel.pehkui.util.MulticonnectCompatibility;
import virtuoel.pehkui.util.ReachEntityAttributesCompatibility;
import virtuoel.pehkui.util.VersionUtils;

public class Pehkui implements ModInitializer
{
	public static final String MOD_ID = "pehkui";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public Pehkui()
	{
		ScaleTypes.INVALID.getClass();
		PehkuiConfig.BUILDER.config.get();
	}
	
	@Override
	public void onInitialize()
	{
		if (ModLoaderUtils.isModLoaded("fabric-command-api-v1"))
		{
			if (VersionUtils.MINOR <= 18)
			{
				CommandUtils.registerArgumentTypes(CommandUtils::registerConstantArgumentType);
			}
			
			PehkuiEntitySelectorOptions.register();
			
			CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicated) ->
			{
				ScaleCommand.register(commandDispatcher, dedicated);
				DebugCommand.register(commandDispatcher, dedicated);
			});
		}
		
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
		{
			if (!server.isHost(handler.player.getGameProfile()))
			{
				ConfigSyncUtils.writeConfigs(handler);
			}
			else
			{
				ConfigSyncUtils.resetSyncedConfigs();
			}
		});
		
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
