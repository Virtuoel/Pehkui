package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgePlayer;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(IForgePlayer.class)
public interface IForgePlayerMixin
{
	@Overwrite(remap = false)
	default double getAttackRange()
	{
		final PlayerEntity self = ((PlayerEntity) this);
		final double range = self.getAttributeValue(ForgeMod.ATTACK_RANGE.get());
		final double ret = range == 0.0 ? 0.0 : range + (double) (self.isCreative() ? 3 : 0);
		
		final float scale = ScaleUtils.getEntityReachScale(self);
		
		return scale != 1.0F ? ret * scale : ret;
	}
	
	@Overwrite(remap = false)
	default double getReachDistance()
	{
		final PlayerEntity self = ((PlayerEntity) this);
		final double reach = self.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
		final double ret = reach == 0.0 ? 0.0 : reach + (self.isCreative() ? 0.5 : 0.0);
		
		final float scale = ScaleUtils.getBlockReachScale(self);
		
		return scale != 1.0F ? ret * scale : ret;
	}
}