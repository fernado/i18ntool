/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package iceworld.fernado.ext.util;

import iceworld.fernado.ext.file.FileCharsetDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public final class ExtUtils {
	
	private ExtUtils() {
	}
	
	/**
	 * 
	 * @param path
	 * @return Map<file name, charset name>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> decodeFilesInFolder(final String path) throws IOException {
		File filePath = new File(path);
		File[] files = filePath.listFiles();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {
			resultMap.put(files[i].getName(), decodeFile(files[i]));
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @param path
	 * @return charset name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String decodeFile(final String path) throws IOException {
		return decodeFile(new File(path));
	}

	/**
	 * 
	 * @param file
	 * @return charset name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String decodeFile(final File file) throws IOException {
		return new FileCharsetDetector().parseFileEncoding(file);
	}
	

}
