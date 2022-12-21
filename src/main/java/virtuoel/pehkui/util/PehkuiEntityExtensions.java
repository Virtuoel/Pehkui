package virtuoel.pehkui.util;

import java.util.Map;

import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface PehkuiEntityExtensions
{
	ScaleData pehkui_getScaleData(ScaleType type);
	
	Map<ScaleType, ScaleData> pehkui_getScales();
	
	boolean pehkui_shouldSyncScales();
	
	void pehkui_setShouldSyncScales(boolean sync);
	
	boolean pehkui_shouldIgnoreScaleNbt();
	
	void pehkui_setShouldIgnoreScaleNbt(boolean ignore);
	
	void pehkui_readScaleNbt(NbtCompound nbt);
	
	NbtCompound pehkui_writeScaleNbt(NbtCompound nbt);
	
	boolean pehkui_isFirstUpdate();
	
	boolean pehkui_getOnGround();
	
	void pehkui_setOnGround(boolean onGround);
}
