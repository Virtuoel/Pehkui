package virtuoel.pehkui.mixin.compat.identity;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.entity.ResizableEntity;
import virtuoel.pehkui.util.CombinedScaleData;
import virtuoel.pehkui.util.IdentityCompatibility;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ResizableEntity
{
	@Unique
	private static final ScaleData[] EMPTY = {};
	
	@Override
	public ScaleData pehkui_constructScaleData()
	{
		return new CombinedScaleData(Optional.of(((Entity) (Object) this)::calculateDimensions), () ->
		{
			final LivingEntity identity = IdentityCompatibility.INSTANCE.getIdentity((PlayerEntity) (Object) this);
			
			return identity == null ? EMPTY : new ScaleData[] { ScaleData.of(identity) };
		});
	}
}
