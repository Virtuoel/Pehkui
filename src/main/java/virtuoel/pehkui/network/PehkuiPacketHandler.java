package virtuoel.pehkui.network;

import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import virtuoel.pehkui.Pehkui;

public class PehkuiPacketHandler
{
	private static final String PROTOCOL_VERSION = Integer.toString(2);
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		Pehkui.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
	);
	
	public static void init()
	{
		int regId = 0;
		INSTANCE.registerMessage(regId++, ScalePacket.class, ScalePacket::encode, ScalePacket::new, ScalePacket::handle);
		INSTANCE.registerMessage(regId++, DebugPacket.class, DebugPacket::encode, DebugPacket::new, DebugPacket::handle);
	}
}
