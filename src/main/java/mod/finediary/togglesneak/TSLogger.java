package mod.finediary.togglesneak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.finediary.togglesneak.TSModContainer.Version;

public class TSLogger {

	private final static boolean isDebug = true;

	private static Logger logger = LogManager.getLogger(Version.MODID);

	public static void debug(String message){
		if (isDebug) info(message);
	}

	public static void info(String message){
		logger.info(message);
	}
}
