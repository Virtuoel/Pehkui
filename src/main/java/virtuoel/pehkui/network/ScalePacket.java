package virtuoel.pehkui.network;

import java.util.Collection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.ScaleUtils;

public class ScalePacket implements CustomPayload
{
	final int id, quantity;
	final Identifier[] typeIds;
	
	NbtCompound[] nbt = null;
	
	ScaleData[] scaleData = null;
	
	public ScalePacket(Entity entity, Collection<ScaleData> scales)
	{
		this.id = entity.getId();
		this.quantity = scales.size();
		
		this.scaleData = scales.toArray(new ScaleData[quantity]);
		this.typeIds = new Identifier[quantity];
		this.nbt = new NbtCompound[quantity];
		
		for (int i = 0; i < quantity; i++)
		{
			this.typeIds[i] = ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, this.scaleData[i].getScaleType());
			this.scaleData[i].writeNbt(this.nbt[i] = new NbtCompound());
		}
	}
	
	protected ScalePacket(PacketByteBuf buf)
	{
		this.id = buf.readVarInt();
		this.quantity = buf.readInt();
		
		this.typeIds = new Identifier[quantity];
		this.nbt = new NbtCompound[quantity];
		
		for (int i = 0; i < quantity; i++)
		{
			this.typeIds[i] = buf.readIdentifier();
			
			this.nbt[i] = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
		}
	}
	
	public static void handle(ScalePacket msg, PlayPayloadContext ctx)
	{
		ctx.workHandler().execute(() ->
		{
			if (FMLEnvironment.dist == Dist.CLIENT)
			{
				final MinecraftClient client = MinecraftClient.getInstance();
				final Entity entity = client.world.getEntityById(msg.id);
				
				if (entity != null)
				{
					for (int i = 0; i < msg.quantity; i++)
					{
						if (ScaleRegistries.SCALE_TYPES.containsKey(msg.typeIds[i]))
						{
							ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, msg.typeIds[i]).getScaleData(entity).readNbt(msg.nbt[i]);
						}
					}
				}
			}
		});
	}
	
	@Override
	public Identifier id()
	{
		return Pehkui.SCALE_PACKET;
	}
	
	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeVarInt(id);
		buf.writeInt(quantity);
		
		for (int i = 0; i < quantity; i++)
		{
			buf.writeIdentifier(typeIds[i]);
			scaleData[i].toPacket(buf);
		}
	}
}
