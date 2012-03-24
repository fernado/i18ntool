/**
 * This class can sort the properties files
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package iceworld.fernado.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

@SuppressWarnings("serial")
public class SortedProperties extends Properties {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration keys() {
		synchronized(this) {
			Enumeration<?> keysEnum = super.keys();
			Vector<String> keyList = new Vector<String>();
			while (keysEnum.hasMoreElements()) {
				keyList.add((String) keysEnum.nextElement());
			}
			Collections.sort(keyList);
			return keyList.elements();
		}
	}
}
