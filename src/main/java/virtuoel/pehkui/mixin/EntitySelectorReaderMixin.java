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
import virtuoel.pehkui.util.PehkuiEntitySelectorReaderExtensions;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin implements PehkuiEntitySelectorReaderExtensions
{
	@Shadow
	abstract void setPredicate(Predicate<Entity> predicate);
	
	@Unique
	NumberRange.FloatRange scaleRange = NumberRange.FloatRange.ANY;
	@Unique
	NumberRange.FloatRange computedScaleRange = NumberRange.FloatRange.ANY;
	@Unique
	ScaleType scaleType = ScaleType.INVALID;
	@Unique
	ScaleType computedScaleType = ScaleType.INVALID;
	
	@Inject(method = "buildPredicate", at = @At("HEAD"))
	private void onBuildPredicate(CallbackInfo info)
	{
		if (!this.scaleRange.isDummy())
		{
			final ScaleType scaleType = this.scaleType == ScaleType.INVALID ? ScaleType.BASE : this.scaleType;
			setPredicate(e -> this.scaleRange.test(scaleType.getScaleData(e).getBaseScale()));
		}
		
		if (!this.computedScaleRange.isDummy())
		{
			final ScaleType scaleType = this.computedScaleType == ScaleType.INVALID ? ScaleType.BASE : this.computedScaleType;
			setPredicate(e -> this.computedScaleRange.test(scaleType.getScaleData(e).getScale()));
		}
	}
	
	@Override
	public ScaleType pehkui_getScaleType()
	{
		return this.scaleType;
	}
	
	@Override
	public void pehkui_setScaleType(final ScaleType scaleType)
	{
		this.scaleType = scaleType;
	}
	
	@Override
	public NumberRange.FloatRange pehkui_getScaleRange()
	{
		return this.scaleRange;
	}
	
	@Override
	public void pehkui_setScaleRange(final NumberRange.FloatRange baseScaleRange)
	{
		this.scaleRange = baseScaleRange;
	}
	
	@Override
	public ScaleType pehkui_getComputedScaleType()
	{
		return this.computedScaleType;
	}
	
	@Override
	public void pehkui_setComputedScaleType(final ScaleType computedScaleType)
	{
		this.computedScaleType = computedScaleType;
	}
	
	@Override
	public NumberRange.FloatRange pehkui_getComputedScaleRange()
	{
		return this.computedScaleRange;
	}
	
	@Override
	public void pehkui_setComputedScaleRange(final NumberRange.FloatRange computedScaleRange)
	{
		this.computedScaleRange = computedScaleRange;
	}
}
