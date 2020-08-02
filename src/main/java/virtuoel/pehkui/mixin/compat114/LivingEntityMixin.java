package virtuoel.pehkui.mixin.compat114;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.mixin.EntityMixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = "method_6078", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity onDeathModifyEntity(Entity entity)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledItemDrops"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				final ScaleData data = ScaleData.of(entity);
				data.setScale(scale);
				data.setTargetScale(scale);
				data.markForSync();
			}
		}
		
		return entity;
	}
	
	@ModifyArg(method = "method_6108", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity updatePostDeathModifyEntity(Entity entity)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledItemDrops"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				final ScaleData data = ScaleData.of(entity);
				data.setScale(scale);
				data.setTargetScale(scale);
				data.markForSync();
			}
		}
		
		return entity;
	}
}
