package virtuoel.pehkui;

import java.util.UUID;
import java.util.function.BiConsumer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.FabricApiCompatibility;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		final BiConsumer<MinecraftClient, PacketByteBuf> handler = (client, buf) ->
		{
			final UUID uuid = buf.readUuid();
			final Identifier typeId = buf.readIdentifier();
			
			final CompoundTag scaleData = ScaleData.fromPacketByteBufToTag(buf);
			
			if (!ScaleRegistries.SCALE_TYPES.containsKey(typeId))
			{
				return;
			}
			
			client.execute(() ->
			{
				for (final Entity e : client.world.getEntities())
				{
					if (e.getUuid().equals(uuid))
					{
						ScaleData.of(e, ScaleRegistries.SCALE_TYPES.get(typeId)).fromTag(scaleData);
						break;
					}
				}
			});
		};
		
		if (FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1"))
		{
			FabricApiCompatibility.Client.registerScalePacket(handler);
		}
		else if (FabricLoader.getInstance().isModLoaded("fabric-networking-v0"))
		{
			FabricApiCompatibility.Client.registerV0ScalePacket(handler);
		}
		else
		{
			Pehkui.LOGGER.fatal("Failed to register scale packet handler! Is Fabric API's networking module missing?");
		}
	}
}
