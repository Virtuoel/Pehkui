package virtuoel.pehkui.mixin.client.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/ItemEntity;)V")
	private void onConstruct(ItemEntity entity, CallbackInfo info)
	{
		ScaleUtils.loadScale((Entity) (Object) this, entity);
	}
}
