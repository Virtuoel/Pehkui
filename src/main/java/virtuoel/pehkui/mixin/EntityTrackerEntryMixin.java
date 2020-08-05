package virtuoel.pehkui.mixin;

import java.util.Map.Entry;

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
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin
{
	@Shadow @Final Entity entity;
	@Shadow abstract void sendSyncPacket(Packet<?> packet);
	
	@Inject(at = @At("TAIL"), method = "tick")
	private void onTick(CallbackInfo info)
	{
		ScaleData scaleData;
		for (Entry<Identifier, ScaleType> entry : ScaleType.REGISTRY.entrySet())
		{
			scaleData = ScaleData.of(entity, entry.getValue());
			
			if (scaleData.shouldSync())
			{
				sendSyncPacket(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET,
					scaleData.toPacketByteBuf(
						new PacketByteBuf(Unpooled.buffer())
						.writeUuid(entity.getUuid())
						.writeIdentifier(entry.getKey())
					)
				));
				scaleData.scaleModified = false;
			}
		}
	}
	
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 7.62939453125E-6D))
	private double tickModifyMinimumSquaredDistance(double value)
	{
		final double scale = ScaleUtils.getMotionScale(entity);
		
		return scale < 1.0D ? value * scale * scale : value;
	}
}
