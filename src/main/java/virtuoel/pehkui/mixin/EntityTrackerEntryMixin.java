package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.PacketByteBuf;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin
{
	@Shadow @Final Entity entity;
	@Shadow abstract void method_18758(Packet<?> packet_1);
	
	@Inject(at = @At("TAIL"), method = "method_18756")
	private void onMethod_18756(CallbackInfo info)
	{
		final ResizableEntity e = ((ResizableEntity) entity);
		if(e.shouldSyncScale())
		{
			method_18758(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET, e.scaleToPacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeUuid(entity.getUuid()))));
		}
	}
}
