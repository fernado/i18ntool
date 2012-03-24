import iceworld.fernado.ext.file.FileCharsetDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;


public class Test {

	/**
	 * @param args
	 */

	public static void main(String[] args) throws IOException {
		String file = "C:/Users/fernado/Desktop/M2-Dec.txt";
		
		FileCharsetDetector fcd = new FileCharsetDetector();
		System.out.println(fcd.parseFileEncoding(file));
		
		InputStream fi = new FileInputStream(
				"C:/Users/fernado/Desktop/M2-Dec.txt");
		Reader ir = new InputStreamReader(fi, "iso-8859-15");
		OutputStream os = new FileOutputStream(
				"C:/Users/fernado/Desktop/M2-Dec2.txt", true);
		Writer ow = new OutputStreamWriter(os, "utf-8");
		BufferedReader in = new BufferedReader(ir);
		PrintWriter out = new PrintWriter(ow);
		String str;
		while ((str = in.readLine()) != null) {
			out.println(str);
		}
		in.close();
		out.close();

	}

}
