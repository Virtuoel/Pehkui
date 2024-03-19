package virtuoel.pehkui.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import virtuoel.pehkui.Pehkui;

public class PehkuiPacketHandler
{
	@SubscribeEvent
	public static void register(final RegisterPayloadHandlerEvent event)
	{
		final IPayloadRegistrar registrar = event.registrar(Pehkui.MOD_ID).versioned("3.0.0");
		registrar.play(Pehkui.SCALE_PACKET, ScalePacket::new, ScalePacket::handle);
		registrar.play(Pehkui.DEBUG_PACKET, DebugPacket::new, DebugPacket::handle);
	}
}
