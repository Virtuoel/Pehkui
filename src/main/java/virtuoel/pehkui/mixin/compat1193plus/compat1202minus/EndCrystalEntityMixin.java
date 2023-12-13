package virtuoel.pehkui.mixin.compat1193plus.compat1202minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin
{
	@ModifyArg(method = "damage", at = @At(value = "INVOKE", target = MixinConstants.CREATE_EXPLOSION_TYPED, remap = false))
	private float pehkui$damage$createExplosion(float power)
	{
		final float scale = ScaleUtils.getExplosionScale((Entity) (Object) this);
		
		return scale != 1.0F ? power * scale : power;
	}
}
