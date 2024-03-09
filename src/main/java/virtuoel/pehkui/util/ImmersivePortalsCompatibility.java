package virtuoel.pehkui.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ImmersivePortalsCompatibility
{
	private static final boolean IMMERSIVE_PORTALS_LOADED = ModLoaderUtils.isModLoaded("imm_ptl_core") || ModLoaderUtils.isModLoaded("immersive_portals");
	
	public static final ImmersivePortalsCompatibility INSTANCE = new ImmersivePortalsCompatibility();
	
	private final Optional<Class<?>> globalClass;
	private final Optional<Class<?>> renderStatesClass;
	
	private final Optional<Method> getViewBobbingOffsetMultiplier;
	private final Optional<Field> viewBobFactor;
	
	private boolean enabled;
	
	private ImmersivePortalsCompatibility()
	{
		this.enabled = IMMERSIVE_PORTALS_LOADED;
		
		if (this.enabled)
		{
			this.globalClass = ReflectionUtils.getClass(
				"qouteall.imm_ptl.core.IPGlobal",
				"qouteall.imm_ptl.core.Global",
				"com.qouteall.immersive_portals.Global"
			);
			
			if (this.globalClass.isPresent())
			{
				ReflectionUtils.setField(globalClass, "enableDepthClampForPortalRendering", null, true);
			}
			
			this.renderStatesClass = ReflectionUtils.getClass(
				"qouteall.imm_ptl.core.render.context_management.RenderStates",
				"com.qouteall.immersive_portals.render.context_management.RenderStates"
			);
			
			this.getViewBobbingOffsetMultiplier = ReflectionUtils.getMethod(this.renderStatesClass, "getViewBobbingOffsetMultiplier");
			this.viewBobFactor = ReflectionUtils.getField(this.renderStatesClass, "viewBobFactor");
		}
		else
		{
			this.globalClass = Optional.empty();
			this.renderStatesClass = Optional.empty();
			
			this.getViewBobbingOffsetMultiplier = Optional.empty();
			this.viewBobFactor = Optional.empty();
		}
	}
	
	public double getViewBobbingOffsetMultiplier()
	{
		if (this.enabled)
		{
			if (VersionUtils.MINOR == 17)
			{
				return 0.0D;
			}
			
			if (this.getViewBobbingOffsetMultiplier.isPresent())
			{
				try
				{
					return (double) this.getViewBobbingOffsetMultiplier.get().invoke(null);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return 1.0D;
				}
			}
			
			if (this.viewBobFactor.isPresent())
			{
				try
				{
					return this.viewBobFactor.get().getDouble(null);
				}
				catch (IllegalAccessException | IllegalArgumentException e)
				{
					return 1.0D;
				}
			}
		}
		
		return 1.0D;
	}
}
