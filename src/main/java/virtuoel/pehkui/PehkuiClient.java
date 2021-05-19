package virtuoel.pehkui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.DebugCommand.DebugPacketType;
import virtuoel.pehkui.util.MixinTargetClasses;
import virtuoel.pehkui.util.ScaleUtils;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		if (FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1"))
		{
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.SCALE_PACKET, (client, handler, buf, sender) ->
			{
				final int id = buf.readVarInt();
				final Identifier typeId = buf.readIdentifier();
				
				final NbtCompound scaleData = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
				
				if (!ScaleRegistries.SCALE_TYPES.containsKey(typeId))
				{
					return;
				}
				
				client.execute(() ->
				{
					final Entity e = client.world.getEntityById(id);
					
					if (e != null)
					{
						ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, typeId).getScaleData(e).readNbt(scaleData);
					}
				});
			});
			
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.DEBUG_PACKET, (client, handler, buf, sender) ->
			{
				final DebugPacketType type = buf.readEnumConstant(DebugPacketType.class);
				
				client.execute(() ->
				{
					switch (type)
					{
						case MIXIN_CLASSLOAD_TESTS:
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
