package virtuoel.pehkui;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import virtuoel.pehkui.api.ScaleData;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientSidePacketRegistry.INSTANCE.register(Pehkui.SCALE_PACKET, (packetContext, packetByteBuf) ->
		{
			final MinecraftClient client = MinecraftClient.getInstance();
			final UUID uuid = packetByteBuf.readUuid();
			
			final float scale = packetByteBuf.readFloat();
			final float prevScale = packetByteBuf.readFloat();
			final float fromScale = packetByteBuf.readFloat();
			final float toScale = packetByteBuf.readFloat();
			final int scaleTicks = packetByteBuf.readInt();
			final int totalScaleTicks = packetByteBuf.readInt();
			
			final CompoundTag scaleData = new CompoundTag();
			
			scaleData.putFloat("scale", scale);
			scaleData.putFloat("previous", prevScale);
			scaleData.putFloat("initial", fromScale);
			scaleData.putFloat("target", toScale);
			scaleData.putInt("ticks", scaleTicks);
			scaleData.putInt("total_ticks", totalScaleTicks);
			
			packetContext.getTaskQueue().execute(() ->
			{
				for(final Entity e : client.world.getEntities())
				{
					if(e.getUuid().equals(uuid))
					{
						ScaleData.of(e).fromTag(scaleData);
						break;
					}
				}
			});
		});
	}
}
