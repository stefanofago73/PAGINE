package it.pagine;

import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class PdfProcessContextImpl implements PdfProcessContext {
	//
	private PdfWriter writer;
	//
	private Document doc;
	//
	private Font defaultFont;
	//
	private Map<String, Font> embFonts;

	/**
	 * 
	 * @param embFonts
	 * @param doc
	 * @param writer
	 */
	public PdfProcessContextImpl(Map<String, Font> fonts, Document doc,
			PdfWriter writer) {
		this.embFonts = fonts;
		this.doc = doc;
		this.writer = writer;
		System.out.println(embFonts+"  "+doc+"   "+writer);
	}

	public void setDefaultFont(Font defaultFont) {
		if (defaultFont != null) {
			this.defaultFont = defaultFont;
		}
	}

	@Override
	public Document getDocument() {
		return doc;
	}

	@Override
	public PdfWriter getWriter() {
		return writer;
	}

	@Override
	public Font getEmbeddedFont(String id) {
		return this.embFonts.get(id);
	}

	public void newPage() {
		doc.newPage();
	}

	public void newLine(Font embeddedFont) {
		try {
			doc.add(new Chunk("\n", embeddedFont));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void newLine() {
		if (defaultFont != null) {
			try {
				doc.add(new Chunk("\n", defaultFont));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return;
		}
		throw new RuntimeException("NO DEFAULT FONT SET!");
	}

}// END
