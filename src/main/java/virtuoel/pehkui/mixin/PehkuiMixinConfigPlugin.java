package virtuoel.pehkui.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import virtuoel.pehkui.util.VersionData;

public class PehkuiMixinConfigPlugin implements IMixinConfigPlugin
{
	private static final String MIXIN_PACKAGE = "virtuoel.pehkui.mixin";
	
	@Override
	public void onLoad(String mixinPackage)
	{
		if (!mixinPackage.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package: Expected \"%s\", but found \"%s\".", MIXIN_PACKAGE, mixinPackage)
			);
		}
	}
	
	@Override
	public String getRefMapperConfig()
	{
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		if (!mixinClassName.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package for class \"%s\": Expected \"%s\", but found \"%s\".", targetClassName, MIXIN_PACKAGE, mixinClassName)
			);
		}
		
		if (
			(mixinClassName.contains(".compat114.") && VersionData.MINOR != 14) ||
			(mixinClassName.contains(".compat114plus.") && VersionData.MINOR < 14) ||
			(mixinClassName.contains(".compat114minus.") && VersionData.MINOR > 14) ||
			(mixinClassName.contains(".compat115.") && VersionData.MINOR != 15) ||
			(mixinClassName.contains(".compat115plus.") && VersionData.MINOR < 15) ||
			(mixinClassName.contains(".compat115minus.") && VersionData.MINOR > 15) ||
			(mixinClassName.contains(".compat116.") && VersionData.MINOR != 16) ||
			(mixinClassName.contains(".compat116plus.") && VersionData.MINOR < 16) ||
			(mixinClassName.contains(".compat116minus.") && VersionData.MINOR > 16) ||
			(mixinClassName.contains(".compat117.") && VersionData.MINOR != 17) ||
			(mixinClassName.contains(".compat117plus.") && VersionData.MINOR < 17) ||
			(mixinClassName.contains(".compat117minus.") && VersionData.MINOR > 17)
		)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
		
	}
	
	@Override
	public List<String> getMixins()
	{
		return null;
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
}
