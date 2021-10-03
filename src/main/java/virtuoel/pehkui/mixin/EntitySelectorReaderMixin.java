package virtuoel.pehkui.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.NumberRange;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntitySelectorReaderExtensions;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin implements PehkuiEntitySelectorReaderExtensions
{
	@Shadow
	abstract void setPredicate(Predicate<Entity> predicate);
	
	@Unique
	NumberRange.FloatRange pehkui$scaleRange = NumberRange.FloatRange.ANY;
	@Unique
	NumberRange.FloatRange pehkui$computedScaleRange = NumberRange.FloatRange.ANY;
	@Unique
	ScaleType pehkui$scaleType = ScaleTypes.INVALID;
	@Unique
	ScaleType pehkui$computedScaleType = ScaleTypes.INVALID;
	
	@Inject(method = "buildPredicate", at = @At("HEAD"))
	private void onBuildPredicate(CallbackInfo info)
	{
		if (!this.pehkui$scaleRange.isDummy())
		{
			final ScaleType scaleType = this.pehkui$scaleType == ScaleTypes.INVALID ? ScaleTypes.BASE : this.pehkui$scaleType;
			setPredicate(e -> this.pehkui$scaleRange.test(scaleType.getScaleData(e).getBaseScale()));
		}
		
		if (!this.pehkui$computedScaleRange.isDummy())
		{
			final ScaleType scaleType = this.pehkui$computedScaleType == ScaleTypes.INVALID ? ScaleTypes.BASE : this.pehkui$computedScaleType;
			setPredicate(e -> this.pehkui$computedScaleRange.test(scaleType.getScaleData(e).getScale()));
		}
	}
	
	@Override
	public ScaleType pehkui_getScaleType()
	{
		return this.pehkui$scaleType;
	}
	
	@Override
	public void pehkui_setScaleType(final ScaleType scaleType)
	{
		this.pehkui$scaleType = scaleType;
	}
	
	@Override
	public NumberRange.FloatRange pehkui_getScaleRange()
	{
		return this.pehkui$scaleRange;
	}
	
	@Override
	public void pehkui_setScaleRange(final NumberRange.FloatRange baseScaleRange)
	{
		this.pehkui$scaleRange = baseScaleRange;
	}
	
	@Override
	public ScaleType pehkui_getComputedScaleType()
	{
		return this.pehkui$computedScaleType;
	}
	
	@Override
	public void pehkui_setComputedScaleType(final ScaleType computedScaleType)
	{
		this.pehkui$computedScaleType = computedScaleType;
	}
	
	@Override
	public NumberRange.FloatRange pehkui_getComputedScaleRange()
	{
		return this.pehkui$computedScaleRange;
	}
	
	@Override
	public void pehkui_setComputedScaleRange(final NumberRange.FloatRange computedScaleRange)
	{
		this.pehkui$computedScaleRange = computedScaleRange;
	}
}
