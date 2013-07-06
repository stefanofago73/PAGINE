package it.pagine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lowagie.text.FontFactoryImp;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class ExtendedFontFactoryImpl extends FontFactoryImp {
	//
	private final HashMap<String, List<String>> fileToFonts = new HashMap<String, List<String>>();
	private final HashMap<String, String> fontToFile = new HashMap<String, String>();
	private ArrayList<String> actualList = new ArrayList<String>();
	private String actualPath;

	public int registerDirectory(String dir) {
		return super.registerDirectory(dir);
	}

	public int registerDirectory(String dir, boolean scanSub) {
		return super.registerDirectory(dir, scanSub);
	}

	public void register(String path, String alias) {
		super.register(path, alias);

		if (path.indexOf("TTC,") >= 0) {
			int idx = path.indexOf("TTC,");
			path = path.substring(0, idx + 3);
		}

		if (!path.equals(actualPath)) {
			actualPath = path;
		}

		if (fileToFonts.get(path) == null) {
			actualList = new ArrayList<String>();
			fileToFonts.put(path, actualList);
		}
	}

	public void registerFamily(String familyName, String fullName, String path) {
		super.registerFamily(familyName, fullName, path);
		actualList.add(fullName);
		fontToFile.put(fullName, actualPath);
	}

	public String getFileForFontName(String fontName) {
		return fontToFile.get(fontName);
	}

	public String debug() {
		return "\n\n" + fileToFonts + "\n\n" + fontToFile + "\n\n";
	}

}// END