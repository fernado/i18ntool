/**
 * 
 * @author fernado
 * @date Apr 15, 2011
 */
package iceworld.fernado.util;

import org.eclipse.osgi.util.NLS;

public class Version extends NLS {

	private static final String BUNDLE_NAME = Version.class.getName();
	
	// Do not instantiate
	private Version() {
	}
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Version.class);
	}
	
	public static String VERSION;

}
