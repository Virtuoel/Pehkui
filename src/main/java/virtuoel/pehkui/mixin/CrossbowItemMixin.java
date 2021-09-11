package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin
{
	@ModifyConstant(method = "shoot", constant = @Constant(doubleValue = 0.15000000596046448D))
	private static double shootModifyYOffset(double value, World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated)
	{
		final float scale = ScaleUtils.getEyeHeightScale(shooter);
		
		return scale != 1.0F ? value * scale : value;
	}
}
