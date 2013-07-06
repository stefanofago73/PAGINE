package it.pagine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class Metadata {
	//
	public static final String DEFAULT_TEMPLATE = "it/pagine/defaultMetadataTemplate.xml";
	//
	public static final String DEFAULT_COLOR_PROFILE = "it/pagine/sRGB_IEC61966-2-1_black_scaled.icc";

	public static final String DEFAULT_CREATOR = "P.A.G.I.N.E.";
	public static final String DEFAULT_SUBJECT = "";
	public static final String DEFAULT_KEYWORDS = "";
	public static final String DEFAULT_TITLE = "";
	public static final String DEFAULT_PRODUCER = "";
	public static final String DEFAULT_AUTHOR = "";
	public static final String DEFAULT_CREATION_TOOLS = "P.A.G.I.N.E. + ITEXT 4";

	/**
	 * 
	 * @param templateFilePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadFromFile(String templateFilePath)
			throws IOException {
		File f = new File(templateFilePath);
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		int len = (int) raf.length();
		byte[] buffer = new byte[len];
		raf.readFully(buffer);
		raf.close();
		return buffer;
	}

	/**
	 * 
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadAsResource(String resourceName) throws IOException {
		java.io.InputStream is = Metadata.class.getClassLoader()
				.getResourceAsStream(resourceName);
		return loadFromStream(is);
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadFromStream(InputStream is) throws IOException {
		int val = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((val = is.read()) != -1) {
			out.write(val);
		}
		out.close();
		return out.toByteArray();
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadDefaultTemplate() throws IOException {
		return loadAsResource(DEFAULT_TEMPLATE);
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadDefaultColorProfile() throws IOException {
		return loadAsResource(DEFAULT_COLOR_PROFILE);
	}

	/**
	 * 
	 * @param writer
	 * @return
	 */
	public static MetadataConfig createMetadata(PdfWriter writer) {
		return new MetadataConfig(writer);
	}

}// END
