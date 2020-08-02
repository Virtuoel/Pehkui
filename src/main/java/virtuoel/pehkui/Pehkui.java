package virtuoel.pehkui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.server.command.ScaleCommand;
import virtuoel.pehkui.server.command.arguments.ScaleOperationArgumentType;

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
		ArgumentTypes.register(id("scale_operation").toString(), ScaleOperationArgumentType.class, new ConstantArgumentSerializer<>(ScaleOperationArgumentType::operation));
		
		CommandRegistrationCallback.EVENT.register(ScaleCommand::register);
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	public static final Identifier SCALE_PACKET = id("scale");
}
