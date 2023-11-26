package virtuoel.pehkui.mixin.step_height;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import virtuoel.pehkui.mixin.PehkuiMixinConfigPlugin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = IEntityExtension.class, priority = 1010)
public interface IForgeEntityMixin
{
	@Overwrite(remap = false)
	default float getStepHeight()
	{
		final Entity self = (Entity) this;
		float step = self.getStepHeight();
		if (self instanceof LivingEntity)
		{
			final EntityAttributeInstance attribute = ((LivingEntity) self).getAttributeInstance(NeoForgeMod.STEP_HEIGHT.value());
			if (attribute != null)
			{
				if (PehkuiMixinConfigPlugin.APOTHIC_ATTRIBUTES_LOADED && self instanceof PlayerEntity)
				{
					step = (float) attribute.getValue();
				}
				else
				{
					step = (float) Math.max(0.0, (double) step + attribute.getValue());
				}
			}
		}
		
		final float scale = ScaleUtils.getStepHeightScale(self);
		
		return scale != 1.0F ? step * scale : step;
	}
}
