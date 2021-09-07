package virtuoel.pehkui.mixin.identity.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.CombinedScaleData;
import virtuoel.pehkui.util.IdentityCompatibility;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PehkuiEntityExtensions
{
	@Unique
	private static final ScaleData[] pehkui$EMPTY = {};
	
	@Override
	public ScaleData pehkui_constructScaleData(ScaleType type)
	{
		return new CombinedScaleData(type, (Entity) (Object) this, () ->
		{
			final LivingEntity identity = IdentityCompatibility.INSTANCE.getIdentity((PlayerEntity) (Object) this);
			
			return identity == null ? pehkui$EMPTY : new ScaleData[] { type.getScaleData(identity) };
		});
	}
}
