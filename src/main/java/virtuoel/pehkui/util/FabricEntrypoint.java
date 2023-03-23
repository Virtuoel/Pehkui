package virtuoel.pehkui.util;

import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

public class FabricEntrypoint
{
	public static final String MOD_ID = "pehkui";
	
	public static final ILogger LOGGER = MixinService.getService().getLogger(MOD_ID);
	
	public static void onInitialize()
	{
		LOGGER.error("The Forge version of \"{}\" is currently installed, but you are playing on a Fabric or Quilt instance! Did you download the wrong mod .jar?", MOD_ID);
	}
}
