package virtuoel.pehkui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import net.minecraftforge.versions.mcp.MCPVersion;

public class VersionUtils
{
	private static final List<Predicate<String>> VERSION_PREDICATES = new ArrayList<>();
	
	static
	{
		final int[][] ranges =
		{
			{1, 14, 4},
			{1, 15, 2},
			{1, 16, 5},
			{1, 17, 1},
			{1, 18, 2},
			{1, 19, 4},
			{1, 20, 2},
			{1, 21, 0},
		};
		
		final String prefix = ".compat%s%s";
		
		for (final int[] range : ranges)
		{
			final int major = range[0];
			final int minor = range[1];
			
			final String predicatePrefix = String.format(prefix, major, minor);
			final String predicateMinor = predicatePrefix + ".";
			final String predicateMinorPlus = predicatePrefix + "plus.";
			final String predicateMinorMinus = predicatePrefix + "minus.";
			final String predicateMinorZero = predicatePrefix + "0.";
			final String predicateMinorZeroMinus = predicatePrefix + "0minus.";
			
			VERSION_PREDICATES.add(n -> n.contains(predicateMinor) && VersionUtils.MINOR != minor);
			VERSION_PREDICATES.add(n -> n.contains(predicateMinorPlus) && VersionUtils.MINOR < minor);
			VERSION_PREDICATES.add(n -> n.contains(predicateMinorMinus) && VersionUtils.MINOR > minor);
			VERSION_PREDICATES.add(n -> n.contains(predicateMinorZero) && (VersionUtils.MINOR != minor || VersionUtils.PATCH != 0));
			VERSION_PREDICATES.add(n -> n.contains(predicateMinorZeroMinus) && (VersionUtils.MINOR > minor || (VersionUtils.MINOR == minor && VersionUtils.PATCH > 0)));
			
			final int maxPatch = range[2];
			
			for (int i = 1; i <= maxPatch; i++)
			{
				final int patch = i;
				
				final String predicatePatch = predicatePrefix + patch + ".";
				final String predicatePatchPlus = predicatePrefix + patch + "plus.";
				final String predicatePatchMinus = predicatePrefix + patch + "minus.";
				
				VERSION_PREDICATES.add(n -> n.contains(predicatePatch) && (VersionUtils.MINOR != minor || VersionUtils.PATCH != patch));
				VERSION_PREDICATES.add(n -> n.contains(predicatePatchPlus) && (VersionUtils.MINOR < minor || (VersionUtils.MINOR == minor && VersionUtils.PATCH < patch)));
				VERSION_PREDICATES.add(n -> n.contains(predicatePatchMinus) && (VersionUtils.MINOR > minor || (VersionUtils.MINOR == minor && VersionUtils.PATCH > patch)));
			}
		}
	}
	
	public static boolean shouldApplyCompatibilityMixin(String mixinClassName)
	{
		if (mixinClassName.contains(".compat"))
		{
			for (final Predicate<String> predicate : VERSION_PREDICATES)
			{
				if (predicate.test(mixinClassName))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static final ArtifactVersion MINECRAFT_VERSION = new DefaultArtifactVersion(MCPVersion.getMCVersion());
	public static final int MAJOR = MINECRAFT_VERSION.getMajorVersion();
	public static final int MINOR = MINECRAFT_VERSION.getMinorVersion();
	public static final int PATCH = MINECRAFT_VERSION.getIncrementalVersion();
}
