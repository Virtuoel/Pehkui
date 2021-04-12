package virtuoel.pehkui.network;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.network.NetworkEvent;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.ScaleUtils;

public class ScalePacket
{
	final UUID uuid;
	final Identifier typeId;
	
	CompoundTag nbt = null;
	
	ScaleData scaleData = null;
	
	public ScalePacket(ScaleData scaleData)
	{
		final Entity e = scaleData.getEntity();
		this.uuid = e != null ? e.getUuid() : null;
		this.typeId = ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, scaleData.getScaleType());
		this.scaleData = scaleData;
	}
	
	private ScalePacket(UUID uuid, Identifier typeId, CompoundTag nbt)
	{
		this.uuid = uuid;
		this.typeId = typeId;
		this.nbt = nbt;
	}
	
	public static void handle(ScalePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if (ScaleRegistries.SCALE_TYPES.containsKey(msg.typeId))
			{
				MinecraftClient client = MinecraftClient.getInstance();
				for (final Entity e : client.world.getEntities())
				{
					if (e.getUuid().equals(msg.uuid))
					{
						ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, msg.typeId).getScaleData(e).readNbt(msg.nbt);
						break;
					}
				}
			}
		});
		
		ctx.get().setPacketHandled(true);
		
	}
	
	public static ScalePacket decode(PacketByteBuf buf)
	{
		final UUID uuid = buf.readUuid();
		final Identifier typeId = buf.readIdentifier();
		final CompoundTag scaleData = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
		
		return new ScalePacket(uuid, typeId, scaleData);
	}
	
	public static void encode(ScalePacket msg, PacketByteBuf buf)
	{
		msg.scaleData.toPacket(
			buf.writeUuid(msg.uuid)
			.writeIdentifier(msg.typeId)
		);
	}
}
