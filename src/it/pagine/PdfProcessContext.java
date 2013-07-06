package it.pagine;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Stefano Fago
 * 
 */
public interface PdfProcessContext {

	Document getDocument();

	PdfWriter getWriter();

	void newPage();

	void newLine(Font embeddedFont);

	void newLine();

	Font getEmbeddedFont(String id);
	
}// END
