package virtuoel.pehkui.mixin.step_height;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(IForgeEntity.class)
public interface IForgeEntityMixin
{
	@Overwrite(remap = false)
	default float getStepHeight()
	{
		final Entity self = (Entity) this;
		float step = self.getStepHeight();
		if (self instanceof LivingEntity)
		{
			final EntityAttributeInstance attribute = ((LivingEntity) self).getAttributeInstance(ForgeMod.STEP_HEIGHT_ADDITION.get());
			if (attribute != null)
			{
				step = (float) Math.max(0.0, (double) step + attribute.getValue());
			}
		}
		
		final float scale = ScaleUtils.getStepHeightScale(self);
		
		return scale != 1.0F ? step * scale : step;
	}
}
