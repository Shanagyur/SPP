package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;

public abstract class AbstractScientificPaperBuilder implements ScientificPaperBuilder {

	public final ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException, ConversionToPDFException, IOException {
		return build(new File(sourceFilePath));
	}
	
	public final ScientificPaper build(File paper) throws NotSupportedFileExtensionException, ConversionToPDFException, IOException {
		checkFileExtension(paper);
		File paperPDF = generatePDF(paper);
		String title = extractTitle(paper);
		String paperAbstract = extractAbstarct(paper);
		List<String> keywords = extractKeywords(paper);
		List<Author> authors = extractAuthors(paper);
		return new ScientificPaper(title, paperAbstract, keywords, authors, paperPDF);
	}

	protected abstract void checkFileExtension(File paper) throws NotSupportedFileExtensionException, IOException;
	protected abstract File generatePDF(File paper) throws ConversionToPDFException, IOException;
	protected abstract String extractTitle(File paper);
	protected abstract String extractAbstarct(File paper);
	protected abstract List<String> extractKeywords(File paper);
	protected abstract List<Author> extractAuthors(File paper);
}
