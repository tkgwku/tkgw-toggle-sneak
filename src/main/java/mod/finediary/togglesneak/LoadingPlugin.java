package mod.finediary.togglesneak;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LoadingPlugin implements IFMLLoadingPlugin {

	public static Boolean RUNTIME_DEOBF; // whether Minecraft is obfuscated or not
	public static File modJarLocation; // like 'C:\.minecraft\mods\dummy.jar'

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"mod.finediary.togglesneak.ClassTransform"};
	}

	@Override
	public String getModContainerClass() {
		return "mod.finediary.togglesneak.SpyModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	/*
	 * data {
	 *   boolean runtimeDeobfuscationEnabled	:
	 *   List(?) coremodList					:
	 *   File(?) mcLocation						: unavailable?
	 *   File coremodLocation					: the file of this mod's jar
	 * }
	 *
	 * (not Javadoc)
	 * @see net.minecraftforge.fml.relauncher.IFMLLoadingPlugin#injectData(java.util.Map)
	 */
	@Override
	public void injectData(Map<String, Object> data) {
        RUNTIME_DEOBF = (Boolean)data.get("runtimeDeobfuscationEnabled");
        modJarLocation = (File)data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
