package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ItemPickupParticle.class)
public class ItemPickupParticleMixin
{
	@Shadow(remap = false) @Final @Mutable Entity field_3821;
	@Shadow(remap = false) @Final @Mutable float field_3822;
	
	@ModifyArg(method = "method_3074(Lnet/minecraft/class_287;Lnet/minecraft/class_4184;FFFFFF)V", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3532;method_16436(DDD)D", ordinal = 4, remap = false), remap = false)
	private double buildGeometryModifyOffset(double value)
	{
		final float scale = ScaleData.of(field_3821).getScale(MinecraftClient.getInstance().getTickDelta());
		
		if (scale != 1.0F)
		{
			return (value - field_3822) + (field_3822 * scale);
		}
		
		return value;
	}
}
