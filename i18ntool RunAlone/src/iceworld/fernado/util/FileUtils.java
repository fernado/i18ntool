package iceworld.fernado.util;

import i18ntool.util.MessageAssistant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class FileUtils {

	private FileUtils() {
	}
	
	/**
	 * This is used InputStream to read
	 * @param filePath 
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(final String filePath) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
		} finally {
			if (null != in) {
				in.close();
			}
		}
		return props;
	}
	
	/**
	 * This is used Reader to read, it need the code
	 * @param filePath
	 * @param charsetName "UTF-8"
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(final String filePath, final String charsetName) 
			throws IOException {
		Properties props = new Properties();
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(filePath), charsetName);
			props.load(read);
		} finally {
			if (null != read) {
				read.close();
			}
		}
		return props;
	}
	
	/**
	 * This is used InputStream to read
	 * @param filePath 
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> loadPropertiesToMap(final String filePath, 
			final int index) throws IOException {
		return loadPropertiesToMap(filePath, MessageAssistant.getInstance().getMessageCharsetNames()[index], index);
	}
	
	/**
	 * This is used Reader to read, it need the code
	 * @param filePath
	 * @param charsetName "UTF-8"
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> loadPropertiesToMap(final String filePath, 
			final String charsetName, final int index) throws IOException {
		Map<String, String> propertyMap = new TreeMap<String, String>();
		InputStreamReader read = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filePath));
			read = new InputStreamReader(is, charsetName);
			BufferedReader reader = new BufferedReader(read);
			String line = reader.readLine();
			Utils.readContent(line, propertyMap, reader, index);
			return propertyMap;
		} finally {
			if (null != read) {
				read.close();
			}
			if (null != is) {
				is.close();
			}
		}
	}
	
	/**
	 * this will use the outputStream to do
	 * @param filePath
	 * @param fileContent
	 * @throws IOException
	 */
	public static void writeStringToFile(final String filePath, final String fileContent) 
			throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bos.write(fileContent.getBytes());
			bos.flush();
		} finally {
			if (null != bos) {
				bos.close();
			}
		}
	}
	
	/**
	 * this will use the outputStream to do
	 * @param filePath
	 * @param fileContent
	 * @throws IOException
	 */
	public static void writeStringUsingCharsetToFile(final String filePath, final String fileContent, final String charsetName) 
			throws IOException {
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(filePath),
					charsetName);
			writer.write(fileContent);
			writer.flush();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}

	}
}
