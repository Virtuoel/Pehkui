package virtuoel.pehkui.network;

import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.ScaleUtils;

public class ScalePacket
{
	final int id;
	final Identifier typeId;
	
	CompoundTag nbt = null;
	
	ScaleData scaleData = null;
	
	public ScalePacket(ScaleData scaleData)
	{
		final Entity e = scaleData.getEntity();
		this.id = e != null ? e.getEntityId() : -1;
		this.typeId = ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, scaleData.getScaleType());
		this.scaleData = scaleData;
	}
	
	protected ScalePacket(PacketByteBuf buf)
	{
		id = buf.readInt();
		typeId = buf.readIdentifier();
		nbt = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
	}
	
	public static void handle(ScalePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
			{
				if (ScaleRegistries.SCALE_TYPES.containsKey(msg.typeId))
				{
					@SuppressWarnings("resource")
					final Entity entity = MinecraftClient.getInstance().world.getEntityById(msg.id);
					
					if (entity != null)
					{
						ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, msg.typeId).getScaleData(entity).readNbt(msg.nbt);
					}
				}
			});
		});
		
		ctx.get().setPacketHandled(true);
		
	}
	
	public void encode(PacketByteBuf buf)
	{
		buf.writeInt(id);
		buf.writeIdentifier(typeId);
		scaleData.toPacket(buf);
	}
}
