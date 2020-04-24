package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin
{
	@Shadow @Final Entity entity;
	@Shadow abstract void sendSyncPacket(Packet<?> packet);
	
	@Inject(at = @At("TAIL"), method = "tick")
	private void onTick(CallbackInfo info)
	{
		final ScaleData data = ScaleData.of(entity);
		
		if (data.shouldSync())
		{
			sendSyncPacket(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET, data.toPacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeUuid(entity.getUuid()))));
		}
	}
	
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 7.62939453125E-6D))
	private double tickModifyMinimumSquaredDistance(double value)
	{
		final double scale = ScaleData.of(entity).getScale();
		
		return scale < 1.0D ? value * scale * scale : value;
	}
}
