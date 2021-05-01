package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LlamaSpitEntity.class)
public class LlamaSpitEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/passive/LlamaEntity;)V")
	private void onConstruct(World world, LlamaEntity owner, CallbackInfo info)
	{
		ScaleUtils.setScaleOfProjectile((Entity) (Object) this, owner);
	}
	
	@ModifyConstant(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/passive/LlamaEntity;)V", constant = @Constant(doubleValue = 0.10000000149011612D))
	private double onConstructModifyEyeOffset(double value, World world, LlamaEntity owner)
	{
		final float scale = ScaleUtils.getHeightScale(owner);
		
		return scale != 1.0F ? value * scale : value;
	}
	
	@ModifyConstant(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/passive/LlamaEntity;)V", constant = @Constant(floatValue = 1.0F))
	private float onConstructModifyWidthOffset(float value, World world, LlamaEntity owner)
	{
		final float scale = ScaleUtils.getWidthScale(owner);
		
		return scale != 1.0F ? value * scale : value;
	}
}
