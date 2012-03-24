/**
 * It depends on the JCharDet (http://jchardet.sourceforge.net/)
 * Charset detector in Mozilla is a XPCOM component which receive bytes 
 * as incoming data and base on the bytes of data "guess" what the charset of 
 * the data is and report it to the caller. 
 * 
 * @author fernado  
 * @date 20/10/2010
 */
package iceworld.fernado.ext.file;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

public class FileCharsetDetector {

	private boolean found = false;
	private String encoding = null;

	/**
	 * 
	 * @param file
	 * @return charset name eg：UTF-8,GBK,GB2312 or null
	 * @throws IOException
	 */
	public String parseFileEncoding(final File file) throws IOException {
		return parseFileEncoding(file, new nsDetector());
	}

	/**
	 * 
	 * @param file
	 * @param languageHint 
	 * 			 eg：1 : Japanese; 
	 * 				 2 : Chinese; 
	 * 				 3 : Simplified Chinese;
	 *               4 : Traditional Chinese; 
	 *               5 : Korean; 
	 *               6 : Dont know (default)
	 * @return charset name eg：UTF-8,GBK,GB2312 or null
	 * @throws IOException
	 */
	public String parseFileEncoding(final File file, final int languageHint) throws IOException {
		return parseFileEncoding(file, new nsDetector(languageHint));
	}

	/**
	 * 
	 * @param path
	 * @return charset name eg：UTF-8,GBK,GB2312 or null
	 * @throws IOException
	 */
	public String parseFileEncoding(final String path) throws IOException {
		return parseFileEncoding(new File(path));
	}

	/**
	 * 
	 * @param path
	 * @param languageHint 
	 * 			 eg：1 : Japanese; 
	 * 				 2 : Chinese; 
	 * 				 3 : Simplified Chinese;
	 *               4 : Traditional Chinese; 
	 *               5 : Korean; 
	 *               6 : Dont know (default)
	 * @return charset name eg：UTF-8,GBK,GB2312 or null
	 * @throws IOException
	 */
	public String parseFileEncoding(final String path, final int languageHint) throws IOException {
		return parseFileEncoding(new File(path), languageHint);
	}

	/**
	 * 
	 * @param file
	 * @param det
	 * @return
	 * @throws IOException
	 */
	private String parseFileEncoding(final File file, final nsDetector det) throws IOException {
		// Set an observer...
		// The Notify() will be called when a matching charset is found.
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(final String charset) {
				found = true;
				encoding = charset;
			}
		});

		BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = true;

		while ((len = imp.read(buf, 0, buf.length)) != -1) {
			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(buf, len);

			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done)
				done = det.DoIt(buf, len, false);
		}
		det.DataEnd();

		if (isAscii) {
			encoding = "ASCII";
			found = true;
		}

		if (!found) {
			String[] prob = det.getProbableCharsets();
			if (prob.length > 0) {
				// if there is not result, choose first one
				encoding = prob[0];
			} else {
				return null;
			}
		}
		return encoding;
	}
}
