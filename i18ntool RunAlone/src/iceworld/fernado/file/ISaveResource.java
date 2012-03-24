/**
 * 
 * @author fernado
 * @date Nov 11, 2010
 */
package iceworld.fernado.file;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ISaveResource {
	
	/**
	 * remove resources
	 * @throws IOException
	 */
	void removeResources() throws IOException;
	
	void saveResources() throws IOException, URISyntaxException;
}
