package virtuoel.pehkui.mixin.compat115plus.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.mixin.EntityMixin;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = MixinConstants.DROP_XP, at = @At(value = "INVOKE", target = MixinConstants.SPAWN_ENTITY, remap = false), remap = false)
	private Entity dropXpModifyEntity(Entity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		
		return entity;
	}
}
