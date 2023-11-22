package virtuoel.pehkui.network;

import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
import virtuoel.pehkui.util.I18nUtils;

public class DebugPacket
{
	public static enum Type
	{
		MIXIN_AUDIT,
		GARBAGE_COLLECT
		;
	}
	
	private final Type type;
	
	public DebugPacket(Type type)
	{
		this.type = type;
	}
	
	protected DebugPacket(PacketByteBuf buf)
	{
		Type read;
		
		try
		{
			read = buf.readEnumConstant(Type.class);
		}
		catch (Exception e)
		{
			read = null;
		}
		
		this.type = read;
	}
	
	public static void handle(DebugPacket msg, NetworkEvent.Context ctx)
	{
		final Type type = msg.type;
		
		ctx.enqueueWork(() ->
		{
			if (FMLEnvironment.dist == Dist.CLIENT)
			{
				final MinecraftClient client = MinecraftClient.getInstance();
				
				switch (type)
				{
					case MIXIN_AUDIT:
						client.player.sendMessage(I18nUtils.translate("commands.pehkui.debug.audit.start.client", "Starting Mixin environment audit (client)..."), false);
						MixinEnvironment.getCurrentEnvironment().audit();
						client.player.sendMessage(I18nUtils.translate("commands.pehkui.debug.audit.end.client", "Mixin environment audit (client) complete!"), false);
						break;
					case GARBAGE_COLLECT:
						System.gc();
						break;
					default:
						break;
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		buf.writeEnumConstant(type);
	}
}
