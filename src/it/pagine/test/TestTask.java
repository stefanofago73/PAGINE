package it.pagine.test;

import it.pagine.FontUtils;
import it.pagine.Metadata;
import it.pagine.PAGINE;
import it.pagine.PAGINE.PdfVersion;
import it.pagine.PAGINETask;
import it.pagine.PdfProcessContext;

import java.awt.Color;
import java.util.Calendar;
import java.util.Random;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Stefano Fago
 * 
 */
public class TestTask extends PAGINETask {

	public static String[] LINES = {
			"o be, or not to be: that is the question:",
			"Whether 'tis nobler in the mind to suffer",
			"The slings and arrows of outrageous fortune,",
			"Or to take arms against a sea of troubles,",
			"And by opposing end them? To die: to sleep;",
			"No more; and by a sleep to say we end",
			"The heart-ache and the thousand natural shocks",
			"That flesh is heir to, 'tis a consummation",
			"Devoutly to be wish'd. To die, to sleep;",
			"To sleep: perchance to dream: ay, there's the rub;",
			"For in that sleep of death what dreams may come",
			"When we have shuffled off this mortal coil,",
			"Must give us pause: there's the respect",
			"That makes calamity of so long life;",
			"For who would bear the whips and scorns of time,",
			"The oppressor's wrong, the proud man's contumely,",
			"The pangs of despised love, the law's delay,",
			"The insolence of office and the spurns",
			"That patient merit of the unworthy takes,",
			"When he himself might his quietus make",
			"With a bare bodkin? who would fardels bear,",
			"To grunt and sweat under a weary life,",
			"But that the dread of something after death,",
			"The undiscover'd country from whose bourn",
			"No traveller returns, puzzles the will",
			"And makes us rather bear those ills we have",
			"Than fly to others that we know not of?",
			"Thus conscience does make cowards of us all;",
			"And thus the native hue of resolution",
			"Is sicklied o'er with the pale cast of thought,",
			"And enterprises of great pith and moment",
			"With this regard their currents turn awry,",
			"And lose the name of action." };

	public void processActivity(PdfProcessContext ctx) {
		try {
			System.out.println(ctx);
			Document doc = ctx.getDocument();
			Random rnd = new Random();
			for (int i = 1; i < LINES.length; i++) {
				Font font = ctx.getEmbeddedFont("Verdana_14");
				font.setSize(rnd.nextInt(10) + 12);
				Paragraph p = new Paragraph(LINES[i], font);
				p.setAlignment(Element.ALIGN_CENTER);
				doc.add(p);
				ctx.newLine();
			}

			ctx.newPage();

			Font font = ctx.getEmbeddedFont("Helvetica_12");
			font.setSize(32);
			font.setStyle(Font.ITALIC | Font.BOLD);
			Paragraph p = new Paragraph("Hello World!", font);
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);

			ctx.newPage();

			try {
				PdfPTable table = getTable(20,
						ctx.getEmbeddedFont("Verdana_14"));
				doc.add(table);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ctx.newPage();

			doc.add(Image.getInstance("animated_camel_v1.gif"));

			ctx.newPage();

			font = ctx.getEmbeddedFont("Verdana_14");
			p = new Paragraph("2 Hello World! 2", font);
			p.setAlignment(Element.ALIGN_CENTER);

			doc.add(p);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static PdfPTable getTable(int numOfMovies, Font font)
			throws Exception {

		Random rnd = new Random();

		PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(3);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setColspan(5);
		table.getDefaultCell().setBackgroundColor(Color.RED);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(new Phrase("" + rnd.nextInt(7), font));
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setColspan(1);
		table.getDefaultCell().setBackgroundColor(Color.ORANGE);
		for (int i = 0; i < 2; i++) {
			table.addCell(new Phrase("Location", font));
			table.addCell(new Phrase("Time", font));
			table.addCell(new Phrase("Run Length", font));
			table.addCell(new Phrase("Title", font));
			table.addCell(new Phrase("Year", font));
		}
		table.getDefaultCell().setBackgroundColor(null);
		table.setHeaderRows(3);
		table.setFooterRows(1);
		for (int i = 0; i < numOfMovies; i++) {
			table.addCell(new Phrase("SomeWhere" + rnd.nextInt(10), font));
			table.addCell(new Phrase(String.format("%1$tH:%1$tM",
					Calendar.getInstance()), font));
			table.addCell(new Phrase(
					String.format("%d '", rnd.nextInt(60) + 60), font));
			table.addCell(new Phrase("TITLE_" + rnd.nextInt(20), font));
			int ii = rnd.nextInt(11);
			table.addCell(new Phrase("20" + ((ii < 10 ? "0" + ii : ii)), font));
		}
		return table;
	}

	// ===========================================================================
	//
	//
	//
	// ===========================================================================

	public static void main(String[] args) throws Exception {

		if (args == null || args.length < 1) {
			System.out
					.println("TestTask [FILE TO CREATE]\nExample:java -cp pagine.jar TestTask c:/TestPDF.pdf\n\n");
			System.exit(0);
		}

		TestTask task = new TestTask();

		PAGINE.startNewPdf(PageSize.A4.rotate(), PdfVersion.V_1_4)
				.createColorProfile(Metadata.loadDefaultColorProfile())
				.metadata()
				.addKeyword("pdfa")
				.addKeyword("sample")
				.addKeyword("PAGINE_TEST")
				.author("Stefano Fago")
				.subject("PDF DI PROVA")
				.creator("Stefano Fago")
				.title("DOCUMENTO GENERATO DA P.A.G.I.N.E.")
				.build(Metadata.loadDefaultTemplate())
				.addEmbeddedFont("Verdana_14",
						FontUtils.getFileForFont("Verdana"), 14)
				.addEmbeddedFont("Helvetica_12",
						FontUtils.getFileForFont("Helvetica"), 12)
				.setDocumentFont("Helvetica_12").activity(task)
				.buildAndWriteOnFile(args[0]);
	}

}// END
