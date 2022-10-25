package virtuoel.pehkui.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import virtuoel.pehkui.util.MixinConstants;

@Mixin(targets = MixinConstants.ENTITY, remap = false)
public interface EntityAccessor {
	@Accessor(value = "field_5987", remap = false)
	public double pehkui$getX();

	@Accessor(value = "field_6010", remap = false)
	public double pehkui$getY();

	@Accessor(value = "field_6035", remap = false)
	public double pehkui$getZ();
}
