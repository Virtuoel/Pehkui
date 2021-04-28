package virtuoel.pehkui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.command.PehkuiEntitySelectorOptions;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.network.PehkuiPacketHandler;
import virtuoel.pehkui.server.command.ScaleCommand;

@Mod(Pehkui.MOD_ID)
public class Pehkui
{
	public static final String MOD_ID = "pehkui";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public Pehkui()
	{
		ScaleType.INVALID.getClass();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		ModLoadingContext ctx = ModLoadingContext.get();
		ctx.registerConfig(ModConfig.Type.CLIENT, PehkuiConfig.clientSpec);
		ctx.registerConfig(ModConfig.Type.SERVER, PehkuiConfig.serverSpec);
		ctx.registerConfig(ModConfig.Type.COMMON, PehkuiConfig.commonSpec);
		
		ArgumentTypes.register(id("scale_type").toString(), ScaleTypeArgumentType.class, new ConstantArgumentSerializer<>(ScaleTypeArgumentType::scaleType));
		ArgumentTypes.register(id("scale_modifier").toString(), ScaleModifierArgumentType.class, new ConstantArgumentSerializer<>(ScaleModifierArgumentType::scaleModifier));
		ArgumentTypes.register(id("scale_operation").toString(), ScaleOperationArgumentType.class, new ConstantArgumentSerializer<>(ScaleOperationArgumentType::operation));
		
		PehkuiEntitySelectorOptions.register();
		
		PehkuiPacketHandler.init();
	}
	
	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event)
	{
		ScaleCommand.register(event.getDispatcher());
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
}
