package virtuoel.pehkui.util;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import net.minecraftforge.versions.mcp.MCPVersion;

public class VersionUtils
{
	public static boolean shouldApplyCompatibilityMixin(String mixinClassName)
	{
		if (mixinClassName.contains(".compat"))
		{
			if (
				(mixinClassName.contains(".compat114.") && MINOR != 14) ||
				(mixinClassName.contains(".compat114plus.") && MINOR < 14) ||
				(mixinClassName.contains(".compat114minus.") && MINOR > 14) ||
				(mixinClassName.contains(".compat1140.") && (MINOR != 14 || PATCH != 0)) ||
				(mixinClassName.contains(".compat1140minus.") && (MINOR > 14 || (MINOR == 14 && PATCH > 0))) ||
				(mixinClassName.contains(".compat1141.") && (MINOR != 14 || PATCH != 1)) ||
				(mixinClassName.contains(".compat1141plus.") && (MINOR < 14 || (MINOR == 14 && PATCH < 1))) ||
				(mixinClassName.contains(".compat1141minus.") && (MINOR > 14 || (MINOR == 14 && PATCH > 1))) ||
				(mixinClassName.contains(".compat1142.") && (MINOR != 14 || PATCH != 2)) ||
				(mixinClassName.contains(".compat1142plus.") && (MINOR < 14 || (MINOR == 14 && PATCH < 2))) ||
				(mixinClassName.contains(".compat1142minus.") && (MINOR > 14 || (MINOR == 14 && PATCH > 2))) ||
				(mixinClassName.contains(".compat1143.") && (MINOR != 14 || PATCH != 3)) ||
				(mixinClassName.contains(".compat1143plus.") && (MINOR < 14 || (MINOR == 14 && PATCH < 3))) ||
				(mixinClassName.contains(".compat1143minus.") && (MINOR > 14 || (MINOR == 14 && PATCH > 3))) ||
				(mixinClassName.contains(".compat1144.") && (MINOR != 14 || PATCH != 4)) ||
				(mixinClassName.contains(".compat1144plus.") && (MINOR < 14 || (MINOR == 14 && PATCH < 4))) ||
				(mixinClassName.contains(".compat1144minus.") && (MINOR > 14 || (MINOR == 14 && PATCH > 4))) ||
				
				(mixinClassName.contains(".compat115.") && MINOR != 15) ||
				(mixinClassName.contains(".compat115plus.") && MINOR < 15) ||
				(mixinClassName.contains(".compat115minus.") && MINOR > 15) ||
				(mixinClassName.contains(".compat1150.") && (MINOR != 15 || PATCH != 0)) ||
				(mixinClassName.contains(".compat1150minus.") && (MINOR > 15 || (MINOR == 15 && PATCH > 0))) ||
				(mixinClassName.contains(".compat1151.") && (MINOR != 15 || PATCH != 1)) ||
				(mixinClassName.contains(".compat1151plus.") && (MINOR < 15 || (MINOR == 15 && PATCH < 1))) ||
				(mixinClassName.contains(".compat1151minus.") && (MINOR > 15 || (MINOR == 15 && PATCH > 1))) ||
				(mixinClassName.contains(".compat1152.") && (MINOR != 15 || PATCH != 2)) ||
				(mixinClassName.contains(".compat1152plus.") && (MINOR < 15 || (MINOR == 15 && PATCH < 2))) ||
				(mixinClassName.contains(".compat1152minus.") && (MINOR > 15 || (MINOR == 15 && PATCH > 2))) ||
				
				(mixinClassName.contains(".compat116.") && MINOR != 16) ||
				(mixinClassName.contains(".compat116plus.") && MINOR < 16) ||
				(mixinClassName.contains(".compat116minus.") && MINOR > 16) ||
				(mixinClassName.contains(".compat1160.") && (MINOR != 16 || PATCH != 0)) ||
				(mixinClassName.contains(".compat1160minus.") && (MINOR > 16 || (MINOR == 16 && PATCH > 0))) ||
				(mixinClassName.contains(".compat1161.") && (MINOR != 16 || PATCH != 1)) ||
				(mixinClassName.contains(".compat1161plus.") && (MINOR < 16 || (MINOR == 16 && PATCH < 1))) ||
				(mixinClassName.contains(".compat1161minus.") && (MINOR > 16 || (MINOR == 16 && PATCH > 1))) ||
				(mixinClassName.contains(".compat1162.") && (MINOR != 16 || PATCH != 2)) ||
				(mixinClassName.contains(".compat1162plus.") && (MINOR < 16 || (MINOR == 16 && PATCH < 2))) ||
				(mixinClassName.contains(".compat1162minus.") && (MINOR > 16 || (MINOR == 16 && PATCH > 2))) ||
				(mixinClassName.contains(".compat1163.") && (MINOR != 16 || PATCH != 3)) ||
				(mixinClassName.contains(".compat1163plus.") && (MINOR < 16 || (MINOR == 16 && PATCH < 3))) ||
				(mixinClassName.contains(".compat1163minus.") && (MINOR > 16 || (MINOR == 16 && PATCH > 3))) ||
				(mixinClassName.contains(".compat1164.") && (MINOR != 16 || PATCH != 4)) ||
				(mixinClassName.contains(".compat1164plus.") && (MINOR < 16 || (MINOR == 16 && PATCH < 4))) ||
				(mixinClassName.contains(".compat1164minus.") && (MINOR > 16 || (MINOR == 16 && PATCH > 4))) ||
				
				(mixinClassName.contains(".compat117.") && MINOR != 17) ||
				(mixinClassName.contains(".compat117plus.") && MINOR < 17) ||
				(mixinClassName.contains(".compat117minus.") && MINOR > 17) ||
				(mixinClassName.contains(".compat1170.") && (MINOR != 17 || PATCH != 0)) ||
				(mixinClassName.contains(".compat1170minus.") && (MINOR > 17 || (MINOR == 17 && PATCH > 0))) ||
				(mixinClassName.contains(".compat1171.") && (MINOR != 17 || PATCH != 1)) ||
				(mixinClassName.contains(".compat1171plus.") && (MINOR < 17 || (MINOR == 17 && PATCH < 1))) ||
				(mixinClassName.contains(".compat1171minus.") && (MINOR > 17 || (MINOR == 17 && PATCH > 1))) ||
				
				(mixinClassName.contains(".compat118.") && MINOR != 18) ||
				(mixinClassName.contains(".compat118plus.") && MINOR < 18) ||
				(mixinClassName.contains(".compat118minus.") && MINOR > 18) ||
				(mixinClassName.contains(".compat1180.") && (MINOR != 18 || PATCH != 0)) ||
				(mixinClassName.contains(".compat1180minus.") && (MINOR > 18 || (MINOR == 18 && PATCH > 0))) ||
				false
			)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static final ArtifactVersion MINECRAFT_VERSION = new DefaultArtifactVersion(MCPVersion.getMCVersion());
	public static final int MAJOR = MINECRAFT_VERSION.getMajorVersion();
	public static final int MINOR = MINECRAFT_VERSION.getMinorVersion();
	public static final int PATCH = MINECRAFT_VERSION.getIncrementalVersion();
}
