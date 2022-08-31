package virtuoel.pehkui.network;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
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
	
	public static void handle(DebugPacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		final Type type = msg.type;
		
		ctx.get().enqueueWork(() ->
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
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
			});
		});
		
		ctx.get().setPacketHandled(true);
	}
	
	public void encode(PacketByteBuf buf)
	{
		buf.writeEnumConstant(type);
	}
}
