package it.pagine;

import java.awt.color.ICC_Profile;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * PAGINE means (P)df/(A) (G)enerator (I)text (N)ot (E)nhanced
 * 
 * @author Stefano Fago
 * 
 */
public class PAGINE {
	//
	private static boolean fontsExplored = false;

	//
	public static enum PdfVersion {

		V_1_2(2), V_1_3(3), V_1_4(4), V_1_5(5), V_1_6(6), V_1_7(7);

		private PdfName version;

		PdfVersion(int version) {
			switch (version) {
			case 2:
				this.version = PdfWriter.PDF_VERSION_1_2;
				break;
			case 3:
				this.version = PdfWriter.PDF_VERSION_1_3;
				break;
			case 4:
				this.version = PdfWriter.PDF_VERSION_1_4;
				break;
			case 5:
				this.version = PdfWriter.PDF_VERSION_1_5;
				break;
			case 6:
				this.version = PdfWriter.PDF_VERSION_1_6;
				break;
			case 7:
				this.version = PdfWriter.PDF_VERSION_1_7;
				break;
			default:
				this.version = PdfWriter.PDF_VERSION_1_4;
				break;
			}
		}

		public PdfName version() {
			return version;
		}

		public String description() {
			return version.toString().substring(1);
		}
	}

	//
	private Map<String, Font> fontToUse = new HashMap<String, Font>();
	//
	private ICC_Profile icc;
	//
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//
	private Document doc;
	//
	private PdfWriter writer;
	//
	private String metadata;
	//
	private Font defaultFont;

	// ===========================================================================
	//
	//
	// ===========================================================================

	public static PAGINE startNewPdf(Rectangle sizeFormat, PdfVersion pdfVersion)
			throws IOException, DocumentException {
		PAGINE pagine = new PAGINE();
		pagine.initFontElements();
		pagine.doc = pagine.createDocument(sizeFormat);
		pagine.writer = pagine.createWriter(pagine.baos, pagine.doc);
		pagine.setupWriterProperties(pagine.writer, pdfVersion.version());
		pagine.openDocument(pagine.doc);
		return pagine;
	}

	public PAGINE createColorProfile(byte[] iccProfileData) throws IOException {
		icc = ICC_Profile.getInstance(iccProfileData);
		writer.setOutputIntents("Custom", "", "http://www.color.org",
				"sRGB IEC61966-2.1", icc);
		return this;
	}

	public MetadataConfig metadata() {
		MetadataConfig meta = Metadata.createMetadata(writer);
		meta.relateDocument(this);
		return meta;
	}

	public PAGINE activity(PAGINETask task) {
		PdfProcessContext ctx = createProcessContext(doc, writer);
		task.processActivity(ctx);
		return this;
	}

	public PAGINE metadata(String toSet) {
		this.metadata = toSet;
		return this;
	}

	public PAGINE addEmbeddedFont(String id, String fontFilePath, int size) {
		Font f = FontFactory.getFont(fontFilePath, BaseFont.CP1252,
				BaseFont.EMBEDDED, size);
		fontToUse.put(id, f);
		return this;
	}

	public Font getEmbeddedFont(String id) {
		return fontToUse.get(id);
	}

	public PAGINE removeEmbeddedFont(String id) {
		fontToUse.remove(id);
		return this;
	}

	public PAGINE setDocumentFont(String id, String fontFilePath, int size) {
		Font f = FontFactory.getFont(fontFilePath, BaseFont.CP1252,
				BaseFont.EMBEDDED, size);
		fontToUse.put(id, f);
		defaultFont = f;
		return this;
	}

	public PAGINE setDocumentFont(String id) {
		defaultFont = fontToUse.get(id);
		return this;
	}

	public byte[] build() {
		commitMetadata(writer);
		closeDocument(doc);
		return baos.toByteArray();
	}

	public void buildAndWriteOnFile(String filePathAndName) throws IOException {
		byte[] data = build();
		FileOutputStream fout = new FileOutputStream(filePathAndName);
		fout.write(data);
		fout.flush();
		fout.close();
	}

	// ===========================================================================
	//
	//
	// ===========================================================================

	protected Document createDocument(Rectangle sizeFromPageSize) {
		if (sizeFromPageSize == null) {
			sizeFromPageSize = PageSize.A4;
		}
		Document doc = new Document(sizeFromPageSize);
		return doc;
	}

	protected PdfWriter createWriter(OutputStream out, Document doc)
			throws IOException, DocumentException {
		PdfWriter writer = PdfWriter.getInstance(doc, out);
		return writer;
	}

	protected void setupWriterProperties(PdfWriter writer, PdfName version)
			throws IOException {
		writer.setPdfVersion(PdfWriter.PDF_VERSION_1_4);
		writer.setPDFXConformance(PdfWriter.PDFA1B);
		writer.setTagged();
	}

	protected void openDocument(Document doc) {
		doc.open();
	}

	protected void closeDocument(Document doc) {
		doc.close();
	}

	protected void commitMetadata(PdfWriter writer) {
		writer.setXmpMetadata(metadata.getBytes(Charset.forName("ISO-8859-1")));
	}

	protected void initFontElements() {
		if (!fontsExplored) {
			FontUtils.init();
			FontFactory.registerDirectories();
			FontFactory.getRegisteredFonts();
			FontFactory.getRegisteredFamilies();
			fontsExplored = true;
		}
	}

	protected PdfProcessContextImpl createProcessContext(Document doc,
			PdfWriter writer) {
		PdfProcessContextImpl ctx = new PdfProcessContextImpl(fontToUse, doc,
				writer);
		ctx.setDefaultFont(defaultFont);
		return ctx;
	}

	// ===========================================================================
	//
	//
	// ===========================================================================

}// END