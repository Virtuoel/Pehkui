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
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.server.command.ScaleCommand;
import virtuoel.pehkui.server.command.arguments.ScaleOperationArgumentType;
import virtuoel.pehkui.server.command.arguments.ScaleTypeArgumentType;

public class Pehkui implements ModInitializer
{
	public static final String MOD_ID = "pehkui";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public Pehkui()
	{
		PehkuiConfig.DATA.getClass();
	}
	
	@Override
	public void onInitialize()
	{
		ScaleRegistries.SCALE_TYPES.getClass();
		ScaleType.INVALID.getClass();
		ScaleModifier.IDENTITY.getClass();
		
		if (FabricLoader.getInstance().isModLoaded("fabric-command-api-v1"))
		{
			ArgumentTypes.register(id("scale_type").toString(), ScaleTypeArgumentType.class, new ConstantArgumentSerializer<>(ScaleTypeArgumentType::scaleType));
			ArgumentTypes.register(id("scale_operation").toString(), ScaleOperationArgumentType.class, new ConstantArgumentSerializer<>(ScaleOperationArgumentType::operation));
			
			CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicated) ->
			{
				ScaleCommand.register(commandDispatcher, dedicated);
			});
		}
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	public static final Identifier SCALE_PACKET = id("scale");
}
