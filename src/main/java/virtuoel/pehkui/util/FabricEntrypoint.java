package virtuoel.pehkui.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricEntrypoint
{
	public static final String MOD_ID = "pehkui";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static void onInitialize()
	{
		LOGGER.fatal("The Forge version of \"{}\" is currently installed, but you are playing on a Fabric instance! Did you download the wrong mod .jar?", MOD_ID);
	}
}
