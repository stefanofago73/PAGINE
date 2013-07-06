package it.pagine;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfReader;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class FontUtils {
	//
	public static Set<Object> _fonts;

	/**
	 * 
	 */
	public static final void init() {
		FontFactory.setFontImp(new ExtendedFontFactoryImpl());
	}

	/**
	 * 
	 * @param fontName
	 * @return
	 */
	public static String getFileForFont(String fontName) {
		return ((ExtendedFontFactoryImpl) FontFactory.getFontImp())
				.getFileForFontName(fontName);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void showFontsAvailable(PrintStream pw) {
		if (pw == null) {
			pw = System.out;
		}
		pw.println("-----------------------------------------------------");

		pw.println("FOUND: [" + FontFactory.registerDirectories()
				+ "] FONT FAMILIES");

		pw.println("-----------------------------------------------------");
		Set<?> registeredFonts = FontFactory.getRegisteredFonts();
		pw.println("-----------------------------------------------------");

		pw.println("REGISTERED FONT");
		for (Iterator<?> iterator = registeredFonts.iterator(); iterator
				.hasNext();) {
			Object object = (Object) iterator.next();
			pw.println(object);
		}

		pw.println("-----------------------------------------------------");
		pw.println("REGISTERED FAMILIES");
		pw.println("-----------------------------------------------------");

		_fonts = FontFactory.getRegisteredFamilies();
		TreeSet<Object> orderedFonts = new TreeSet<Object>(_fonts);
		for (Object font : orderedFonts) {
			pw.println(font);
		}
	}

	/**
	 * 
	 * @param filename
	 * @param pw
	 */
	public static void showMetadata(String filename, PrintStream pw) {
		if (pw == null) {
			pw = System.out;
		}
		try {
			PdfReader reader = new PdfReader(filename);
			byte[] buffer = reader.getMetadata();
			if (buffer != null) {
				String meta = new String(buffer);
				pw.println("\nMETADATA:\n " + meta + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}// END