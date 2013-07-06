package it.pagine;

import java.nio.charset.Charset;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Meta;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class MetadataConfig {

	private String author;
	private String producer;
	private String title;
	private String subject;
	private String creator;
	private String creationTool;
	private StringBuffer keywords = new StringBuffer();
	private String modificationDate, creationDate;
	private PdfWriter writer;
	private PAGINE parent;

	public MetadataConfig() {
		initDefault();
	}

	public MetadataConfig(PdfWriter writer) {
		initDefault();
		this.writer = writer;
	}

	public MetadataConfig writer(PdfWriter writer) {
		this.writer = writer;
		return this;
	}

	public MetadataConfig relateDocument(PAGINE pagine) {
		this.parent = pagine;
		return this;
	}

	public MetadataConfig author(String author) {
		this.author = author;
		return this;
	}

	public MetadataConfig producer(String producer) {
		this.producer = producer;
		return this;
	}

	public MetadataConfig subject(String subject) {
		this.subject = subject;
		return this;
	}

	public MetadataConfig title(String title) {
		this.title = title;
		return this;
	}

	public MetadataConfig creator(String creator) {
		this.creator = creator;
		return this;
	}

	public MetadataConfig creatorTool(String creationTool) {
		this.creationTool = creationTool;
		return this;
	}

	public MetadataConfig addKeyword(String keyword) {
		this.keywords.append(keyword).append(",");
		return this;
	}

	public PAGINE build(byte[] metadataTemplate) {
		String result = null;
		try {
			result = new String(metadataTemplate, Charset.forName("ISO-8859-1"));
			keywords.delete(keywords.length() - 1, keywords.length());
			try {
				addBaseMetadata();
			} catch (DocumentException e) {
				throw new RuntimeException(
						"Error Building Metadata [Adding subject,producer,keywords]",
						e);
			}
			createDateFromPDfDates();
			result = result.replaceAll("\\$\\{author\\}", author)
					.replaceAll("\\$\\{title\\}", title)
					.replaceAll("\\$\\{creator\\}", creator)
					.replaceAll("\\$\\{creatorTool\\}", creationTool)
					.replaceAll("\\$\\{creationDate\\}", creationDate)
					.replaceAll("\\$\\{modifyDate\\}", modificationDate)
					.replaceAll("\\$\\{producer\\}", producer)
					.replaceAll("\\$\\{subject\\}", subject)
					.replaceAll("\\$\\{keywords\\}", keywords.toString());
			parent.metadata(result);
		} catch (Exception exc) {
			throw new RuntimeException("Error Building Metadata...", exc);
		}
		return parent;
	}

	// ===================================================================
	//
	//
	// ===================================================================

	protected void initDefault() {
		author = Metadata.DEFAULT_AUTHOR;
		producer = Metadata.DEFAULT_PRODUCER;
		title = Metadata.DEFAULT_TITLE;
		subject = Metadata.DEFAULT_SUBJECT;
		creator = Metadata.DEFAULT_CREATION_TOOLS;
		creationTool = Metadata.DEFAULT_CREATION_TOOLS;
	}

	protected void createDateFromPDfDates() {
		PdfDictionary info = writer.getInfo();
		String modDate = info.getAsString(new PdfName("ModDate")).toString();
		String creatDate = info.getAsString(new PdfName("CreationDate"))
				.toString();

		Date mDate = DateConvertionUtils.fromPdfDateToJavaDate(modDate);
		Date cDate = DateConvertionUtils.fromPdfDateToJavaDate(creatDate);

		creationDate = DateConvertionUtils.toString(cDate);
		modificationDate = DateConvertionUtils.toString(mDate);
	}

	protected void addBaseMetadata() throws DocumentException {
		writer.add(new Meta("subject", subject));
		writer.add(new Meta("keywords", keywords.toString()));
		writer.getInfo().put(new PdfName("Producer"), new PdfString(producer));
	}
}// END