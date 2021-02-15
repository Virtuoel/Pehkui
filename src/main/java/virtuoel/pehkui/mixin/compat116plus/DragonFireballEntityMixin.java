package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin
{
	@ModifyConstant(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", constant = @Constant(doubleValue = 4.0D))
	private double onCollisionModifyWidth(double value)
	{
		final float scale = ScaleUtils.getWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", constant = @Constant(doubleValue = 2.0D))
	private double onCollisionModifyHeight(double value)
	{
		final float scale = ScaleUtils.getHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
