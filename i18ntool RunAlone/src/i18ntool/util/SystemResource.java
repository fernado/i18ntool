/**
 * This class is for initiating the first time i18nTool read the project path
 * 
 * Currently I cant find a simple way to exist both path for deployment and development
 * 
 * The easy way is to write hard code.
 * 
 * @author fernado
 * @date Jan 7, 2011
 */
package i18ntool.util;

import i18ntool.Activator;
import i18ntool.consts.Constant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.util.Utils;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

public final class SystemResource {

	private static final SystemResource instance = new SystemResource();
	
	private String projectFolder;
	
	private SystemResource() {
	}
	
	public static final SystemResource getInstance() {
		return instance;
	}
	
	private boolean isDeploy = false;
	
    /**
     * get the project folder absolute path
     * @return project folder
     */
	public String getProjectFolder() {
		if (!Utils.isEmpty(projectFolder)) {
			return projectFolder;
		}
		
		return isDeploy ? forDeployment() : forDevelopment();
	}
	
	private String forDeployment() {
		projectFolder = Activator.getDefault().getStateLocation().makeAbsolute().toFile().getAbsolutePath();
		if (projectFolder.indexOf(Constant.PRE5_PARENT_FOLDER) < 0) {
			projectFolder = projectFolder + Constant.PRE5_PARENT_FOLDER;
		}
		return projectFolder;
	}
	
	private String forDevelopment() {
		URL bundleRootURL = Activator.getDefault().getBundle().getEntry("/");
		URL pluginURL = null;
		try {
			pluginURL = FileLocator.resolve(bundleRootURL);
		} catch (IOException e) {
			return Constants.EMPTY_STRING;
		}
		return pluginURL.getPath();
	}
	

}
