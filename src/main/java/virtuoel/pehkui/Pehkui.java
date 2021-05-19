package virtuoel.pehkui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.command.PehkuiEntitySelectorOptions;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.ScaleCommand;

public class Pehkui implements ModInitializer
{
	public static final String MOD_ID = "pehkui";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public Pehkui()
	{
		PehkuiConfig.COMMON.getClass();
	}
	
	@Override
	public void onInitialize()
	{
		ScaleType.INVALID.getClass();
		
		if (FabricLoader.getInstance().isModLoaded("fabric-command-api-v1"))
		{
			ArgumentTypes.register(id("scale_type").toString(), ScaleTypeArgumentType.class, new ConstantArgumentSerializer<>(ScaleTypeArgumentType::scaleType));
			ArgumentTypes.register(id("scale_modifier").toString(), ScaleModifierArgumentType.class, new ConstantArgumentSerializer<>(ScaleModifierArgumentType::scaleModifier));
			ArgumentTypes.register(id("scale_operation").toString(), ScaleOperationArgumentType.class, new ConstantArgumentSerializer<>(ScaleOperationArgumentType::operation));
			
			PehkuiEntitySelectorOptions.register();
			
			CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicated) ->
			{
				ScaleCommand.register(commandDispatcher, dedicated);
				DebugCommand.register(commandDispatcher, dedicated);
			});
		}
	}
	
	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + String.join("/", paths));
	}
	
	public static final Identifier SCALE_PACKET = id("scale");
	public static final Identifier DEBUG_PACKET = id("debug");
}
