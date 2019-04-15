package virtuoel.pehkui;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ResizableEntity;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientSidePacketRegistry.INSTANCE.register(SCALE_PACKET, (packetContext, packetByteBuf) ->
		{
			final MinecraftClient client = MinecraftClient.getInstance();
			client.execute(() ->
			{
				final UUID uuid = packetByteBuf.readUuid();
				for(final Entity e : client.world.getEntities())
				{
					if(e.getUuid().equals(uuid))
					{
						((ResizableEntity) e).scaleFromPacketByteBuf(packetByteBuf);
						break;
					}
				}
			});
		});
	}
	
	public static final Identifier SCALE_PACKET = Pehkui.id("scale");
}
