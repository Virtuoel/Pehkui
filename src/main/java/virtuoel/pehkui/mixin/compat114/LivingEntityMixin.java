package virtuoel.pehkui.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.DEATH, args = { DamageSource.class }), at = @At(value = "INVOKE", desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private Entity onDeathModifyEntity(Entity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		
		return entity;
	}
	
	@ModifyArg(target = @Desc(value = MixinConstants.POST_DEATH), at = @At(value = "INVOKE", desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private Entity updatePostDeathModifyEntity(Entity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		
		return entity;
	}
}
