/**
 * This class is used for log, it wraps the java.util.LogManager.
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.util;

import i18ntool.consts.Constant;
import iceworld.fernado.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public final class LogManager {
	
	static {
		InputStream inputStream = null;
		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
		String file = Utils.replaceBacklashAsSlash(
				SystemResource.getInstance().getProjectFolder() 
				+ Constant.CONF_PATH + Constant.LOG_PROPERTIES);
		try {
			inputStream = new FileInputStream(new File(file));
			logManager.readConfiguration(inputStream);
		} catch (SecurityException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	public static Logger getLogger(final String clzName) {
		return Logger.getLogger(clzName);
	}

}