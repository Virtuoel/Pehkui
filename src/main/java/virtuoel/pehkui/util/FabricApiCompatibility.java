package virtuoel.pehkui.util;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import virtuoel.pehkui.Pehkui;

public class FabricApiCompatibility
{
	public static class Client
	{
		public static void registerScalePacket(BiConsumer<MinecraftClient, PacketByteBuf> consumer)
		{
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.SCALE_PACKET, (client, handler, buf, sender) ->
			{
				consumer.accept(client, buf);
			});
		}
		
		public static void registerV0ScalePacket(BiConsumer<MinecraftClient, PacketByteBuf> consumer)
		{
			ClientSidePacketRegistry.INSTANCE.register(Pehkui.SCALE_PACKET, (ctx, buf) ->
			{
				consumer.accept(MinecraftClient.getInstance(), buf);
			});
		}
	}
}
