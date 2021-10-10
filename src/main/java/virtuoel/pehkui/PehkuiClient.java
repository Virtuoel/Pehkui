package virtuoel.pehkui;

import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.DebugCommand.DebugPacketType;
import virtuoel.pehkui.util.I18nUtils;
import virtuoel.pehkui.util.MixinTargetClasses;
import virtuoel.pehkui.util.ModLoaderUtils;
import virtuoel.pehkui.util.ScaleUtils;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		if (ModLoaderUtils.isModLoaded("fabric-networking-api-v1"))
		{
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.SCALE_PACKET, (client, handler, buf, sender) ->
			{
				final int id = buf.readVarInt();
				
				for (int i = buf.readInt(); i > 0; i--)
				{
					final Identifier typeId = buf.readIdentifier();
					
					final NbtCompound scaleData = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
					
					if (!ScaleRegistries.SCALE_TYPES.containsKey(typeId))
					{
						continue;
					}
					
					client.execute(() ->
					{
						final Entity e = client.world.getEntityById(id);
						
						if (e != null)
						{
							ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, typeId).getScaleData(e).readNbt(scaleData);
						}
					});
				}
			});
			
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.DEBUG_PACKET, (client, handler, buf, sender) ->
			{
				DebugPacketType read;
				
				try
				{
					read = buf.readEnumConstant(DebugPacketType.class);
				}
				catch (Exception e)
				{
					read = null;
				}
				
				final DebugPacketType type = read;
				
				client.execute(() ->
				{
					switch (type)
					{
						case MIXIN_AUDIT:
							DebugCommand.runMixinClassloadTests(
								t -> client.player.sendMessage(t, false),
								true,
								false,
								MixinTargetClasses.Common.CLASSES,
								MixinTargetClasses.Client.CLASSES
							);
							
							DebugCommand.runMixinClassloadTests(
								t -> client.player.sendMessage(t, false),
								true,
								true,
								MixinTargetClasses.Common.INTERMEDIARY_CLASSES,
								MixinTargetClasses.Server.INTERMEDIARY_CLASSES
							);
							
							client.player.sendMessage(I18nUtils.translate("commands.pehkui.debug.audit.start", "Starting Mixin environment audit (client)..."), false);
							MixinEnvironment.getCurrentEnvironment().audit();
							client.player.sendMessage(I18nUtils.translate("commands.pehkui.debug.audit.end", "Starting Mixin environment audit (client)..."), false);
							
							break;
						case GARBAGE_COLLECT:
							System.gc();
							break;
						default:
							break;
					}
				});
			});
		}
		else
		{
			Pehkui.LOGGER.fatal("Failed to register Pehkui's packet handlers! Is Fabric API's networking module missing?");
		}
	}
}
