PAGINE means (P)df/(A) (G)enerator (I)text (N)ot (E)nhanced

This little code framework use ITEXT 4 (this is the reason for Not Enhanced)  ;-)


Usage:


Create your task: it's the place where you create your document 
Create a PAGINE instance: 
 - then add a color profile
 - then add metadata
 - then choose the font to use ( take a look for the Font Utilities )
 - then use your task
 - ...and build your PDF!
 
        PAGINETask task = ...

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
				
				
				