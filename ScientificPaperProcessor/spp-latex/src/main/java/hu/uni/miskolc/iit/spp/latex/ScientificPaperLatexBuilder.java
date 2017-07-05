package hu.uni.miskolc.iit.spp.latex;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.UsedDirectoryNames;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedArchiveExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.core.service.AbstractScientificPaperBuilder;
import hu.uni.miskolc.iit.spp.latex.compile.Latex2PDFCompiler;
import hu.uni.miskolc.iit.spp.latex.compile.LatexCompilerFactory;
import hu.uni.miskolc.iit.spp.latex.fileOperations.DestinationDirectory;
import hu.uni.miskolc.iit.spp.latex.fileOperations.Unpacking;

public class ScientificPaperLatexBuilder extends AbstractScientificPaperBuilder {
	
	private static final String DESTIONATION_DIR_NAME = UsedDirectoryNames.DIR_FOR_PDF_FILE.getStringValue();
	
	private File directoryForTexFile;
	private File directoryForPDFFile;
	
	public ScientificPaperLatexBuilder() {
	}

	@Override
	protected void checkFileExtension(File paper) throws NotSupportedFileExtensionException, IOException {
		try {
			this.directoryForTexFile = Unpacking.unzip(paper);
		} catch (NotSupportedArchiveExtensionException e) {
			throw new NotSupportedFileExtensionException(e.getMessage());
		}
	}
	@Override
	protected File generatePDF(File paper) throws ConversionToPDFException, IOException {
		try {	
			LatexCompilerFactory factory = new LatexCompilerFactory();
			Latex2PDFCompiler compiler = factory.createLatexPDFCompiler();
			this.directoryForPDFFile = DestinationDirectory.createDestinationDir(paper, DESTIONATION_DIR_NAME);
			File pdfFile = compiler.generatePDFFile(this.directoryForTexFile, this.directoryForPDFFile);
			return pdfFile;
		} catch (NotSupportedOperationSystemException e) {
			throw new ConversionToPDFException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	@Override
	protected String extractTitle(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String extractAbstarct(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> extractKeywords(File paper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Author> extractAuthors(File paper) {
		// TODO Auto-generated method stub
		return null;
	}
}