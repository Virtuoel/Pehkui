package virtuoel.pehkui.entity;

import java.util.Optional;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

public interface ResizableEntity
{
	default ScaleData pehkui_constructScaleData()
	{
		return new ScaleData(Optional.of(((Entity) (Object) this)::calculateDimensions));
	}
	
	default ScaleData pehkui_getScaleData()
	{
		return ScaleData.IDENTITY;
	}
}
