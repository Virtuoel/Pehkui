package virtuoel.pehkui.util;

import java.util.Optional;

public class ImmersivePortalsCompatibility
{
	private static final boolean IMMERSIVE_PORTALS_LOADED = ModLoaderUtils.isModLoaded("imm_ptl_core") || ModLoaderUtils.isModLoaded("immersive_portals");
	
	public static final ImmersivePortalsCompatibility INSTANCE = new ImmersivePortalsCompatibility();
	
	private final Optional<Class<?>> globalClass;
	
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
		}
		else
		{
			this.globalClass = Optional.empty();
		}
	}
}
