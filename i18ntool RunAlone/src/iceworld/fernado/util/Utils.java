/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package iceworld.fernado.util;

import iceworld.fernado.consts.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;


public final class Utils {
	
	private Utils() {
	}

	/**
	 * Init the strs' values are all ""
	 * @param strs
	 * @return
	 */
	public static String[] initArrayOfStringIfNull(final String[] strs) {
		for (int i = 0; i < strs.length; i++) {
			if (null == strs[i]) {
				strs[i] = Constants.EMPTY_STRING;
			}
		}
		return strs;
	}

	/**
	 * Init the arrs' values are all false
	 * @param arrs
	 * @return
	 */
	public static boolean[] initArrayOfBoolean(final boolean[] arrs) {
		for (int i = 0; i < arrs.length; i++) {
			arrs[i] = false;
		}
		return arrs;
	}

	/**
	 * There is no trim between 2 params
	 * if str1 is the same as str2, return true
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isSame(final String str1, final String str2) {
		if (null == str1 && null == str2) {
			return true;
		}
		if (Constants.EMPTY_STRING.equals(str1)) {
			return Constants.EMPTY_STRING.equals(str2);
		}
		if (Constants.EMPTY_STRING.equals(str2)) {
			return Constants.EMPTY_STRING.equals(str1);
		}
		return (str1.equals(str2));
	}

	/**
	 * If strs1's elements are the same as strs2, return true
	 * @param strs1
	 * @param strs2
	 * @return
	 */
	public static boolean isSame(final String[] strs1, final String[] strs2) {
		for (int i = 0; i < strs1.length; i++) {
			if (false == isSame(strs1[i], strs2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * If str is null or empty, return true
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final String str) {
		return (null == str || Constants.EMPTY_STRING.equals(str));
	}

	/**
	 * If objs is null or the length of it is 0, return true
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty(final Object[] objs) {
		return (null == objs || 0 == objs.length);
	}

	/**
	 * The collection is null or the size of it is 0, return true
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(final Collection<?> collection) {
		return (null == collection || 0 == collection.size());
	}
	
	/**
	 * The map is null or the size is 0, return true 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(final Map<?, ?> map) {
		return (null == map || 0 == map.size());
	}
	
	/**
	 * if strs contains empty or null element, return true
	 * @param strs
	 * @return
	 */
	public static boolean containsEmpty(final String[] strs) {
		for (String str: strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create a folder name file
	 * @param file
	 */
	public static void createFolder(final String file) {
		File folder = new File(file);
		if (!folder.exists()) {
			folder.mkdir();
		}
	}
	
	/**
	 * <pre>
	 * project_folder\resource\export\message.properties
	 * currently there only has projet_folder, so it will create the export folder,
	 * create the resource folder and last create the message.properties file
	 * </pre>
	 * @param folder
	 * @param file
	 * @throws IOException
	 */
	public static void createFolderAndFile(final String folder, final String file) throws IOException {
		String[] files = file.split(Constants.SLASH);
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (int i = 0; i < files.length; i++) {
			sb.append(files[i]).append(Constants.SLASH);
			if (folder.equals(sb.toString())) {
				flag = true;
				continue;
			}
			if (flag) {
				File f = new File(sb.toString());
				if (i != files.length - 1) {
					if (!f.exists()) {
						f.mkdir();
					}
				} else {
					if (!f.exists()) {
						f.createNewFile();
					}
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * project_folder\resource\export\exported.xls
	 * currently there only has projet_folder, so it will create the export folder,
	 * create the resource folder and last create the message.properties file
	 * </pre>
	 * @param folder
	 * @param file
	 * @throws IOException
	 */
	public static void createFolder(final String folder, final String file) throws IOException {
		String[] files = file.split(Constants.SLASH);
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (int i = 0; i < files.length; i++) {
			sb.append(files[i]).append(Constants.SLASH);
			if (folder.equals(sb.toString())) {
				flag = true;
				continue;
			}
			if (flag) {
				File f = new File(sb.toString());
				if (i != files.length - 1) {
					if (!f.exists()) {
						f.mkdir();
					}
				}
			}
		}
	}
	
	/**
	 * Remove the filePath's files, if it contains folder, remove it's sub folder 
	 * @param filePath
	 * @throws IOException
	 */
	public static void removeFiles(final String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			if (file.listFiles().length == 0) {
				file.delete();
			} else {
				File[] files = file.listFiles();
				int length = file.listFiles().length;
				for (int i = 0; i < length; i++) {
					if (files[i].isDirectory()) {
						removeFiles(files[i].getAbsolutePath());
					}
					files[i].delete();
				}
			}
		}
	}
	
	/**
	 * for all the path, replace "\" with "/"
	 * 
	 * @param param
	 * @return
	 */
	public static String replaceBacklashAsSlash(final String param) {
		return param.replaceAll(Constants.FOUR_BACKLASH, Constants.SLASH);
	}
	
	public static void readContent(String line, Map<String, String> map, BufferedReader reader, int index) throws IOException {
		StringBuilder builder = new StringBuilder();
		int lineNumber = 0;
		while (line != null) {
			lineNumber++;
			if (0 == index) {
				boolean flagComment = false;
				while (hasComment(line, builder, lineNumber)) {
					line = reader.readLine();
					lineNumber++;
					flagComment = true;
				}
				if (flagComment) {
					builder.delete(builder.length() - getLF().length(), builder.length());
				}
			}
			Utils.dispartKeyAndValue(line, map, builder);
			line = reader.readLine();
		}
	}
	
	public static boolean hasComment(String str, StringBuilder builder, int lineNumber) {
		boolean result = false;
		if (!Utils.isEmpty(str)) {
			final String line = str.trim();
			int poundIndex = line.indexOf(Constants.POUND);
			if (poundIndex == 0 && line.length() > 0) {
				if (lineNumber > 2) {
					builder.append(line.substring(1)).append(getLF());
				}
				result = true;
			}
		}
		return result;
	}
	
	public static void dispartKeyAndValue(String str, Map<String, String> map, StringBuilder builder) {
		if (!Utils.isEmpty(str)) {
			final int equalIndex = str.indexOf(Constants.EQUAL);
			if (equalIndex > -1) {
				final String key = str.substring(0, equalIndex);
				final String keyComment = commentOfKey(key);
				if (key.indexOf(Constants.POUND) == -1) {
					String value = str.substring(equalIndex + 1);
					map.put(key, value);
					map.put(keyComment, builder.toString());
					builder.delete(0, builder.length());
				}
			} else {
				builder.delete(0, builder.length());
			}
		}
	}
	
	public static String commentOfKey(String key) {
		return key + Constants.COMMENT_SUFFIX;
	}
	
	/**
	 * 
	 * @param filePath  this is a folder path
	 * @param suffix  eg: .properties
	 * @return all the files under the filePath, which suffix is .properties else return null;
	 */
	public static String[] getFilesWithSuffix(final String filePath, final String suffix) {
		String[] results = null;
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdir();
		}
		results = folder.list(new FilenameFilter() {
			public boolean accept(final File dir, final String name) {
				if (name.endsWith(suffix) || name.endsWith(suffix.toLowerCase())) {
					return true;
				} else {
					return false;
				}
			}
		});
		return results;
	}
	
	public static String getFileWithSuffix(final String filePath, final String suffix) {
		String result = Constants.EMPTY_STRING;
		File file = new File(filePath);
		if (file.isFile()) {
			return filePath;
		}
		return result;
	}

	/**
	 * current time
	 * like 11/12/2010 14:40 as 20101112-1440
	 * @return
	 */
	public static String getCurrentVersionId() {
		return Version.VERSION;
	}
	
	/**
	 * 
	 * @return \r\n if current system is windows
	 * else \n
	 */
	public static String getLF() {
		if (isWindowsOS()) {
			return Constants.WINDOWS_CR_LF;
		} else {
			return Constants.LINUX_LF;
		}
	}
	
	/**
	 * 
	 * @return true if current system is windows
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
	
}
