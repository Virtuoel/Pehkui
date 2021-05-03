package virtuoel.pehkui.mixin.pehkui;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleType.Builder;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
@Mixin(ScaleType.Builder.class)
public abstract class ScaleTypeBuilderMixin
{
	@Shadow(prefix = "pehkui$", remap = false)
	public abstract Builder pehkui$defaultBaseScale(float defaultBaseScale);
	
	@Shadow(prefix = "pehkui$", remap = false)
	public abstract Builder pehkui$defaultTickDelay(int defaultTickDelay);
	
	public void defaultBaseScale(float defaultBaseScale)
	{
		pehkui$defaultBaseScale(defaultBaseScale);
	}
	
	public void defaultTickDelay(int defaultTickDelay)
	{
		pehkui$defaultTickDelay(defaultTickDelay);
	}
}
