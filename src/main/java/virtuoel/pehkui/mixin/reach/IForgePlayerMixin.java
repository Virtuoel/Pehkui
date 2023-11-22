package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.player.PlayerEntity;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(IPlayerExtension.class)
public interface IForgePlayerMixin
{
	@Overwrite(remap = false)
	default double getEntityReach()
	{
		final PlayerEntity self = ((PlayerEntity) this);
		final double range = self.getAttributeValue(NeoForgeMod.ENTITY_REACH.value());
		final double ret = range == 0.0 ? 0.0 : range + (double) (self.isCreative() ? 3 : 0);
		
		final float scale = ScaleUtils.getEntityReachScale(self);
		
		return scale != 1.0F ? ret * scale : ret;
	}
	
	@Overwrite(remap = false)
	default double getBlockReach()
	{
		final PlayerEntity self = ((PlayerEntity) this);
		final double reach = self.getAttributeValue(NeoForgeMod.BLOCK_REACH.value());
		final double ret = reach == 0.0 ? 0.0 : reach + (self.isCreative() ? 0.5 : 0.0);
		
		final float scale = ScaleUtils.getBlockReachScale(self);
		
		return scale != 1.0F ? ret * scale : ret;
	}
}
