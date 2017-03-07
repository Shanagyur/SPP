package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.util.List;

import hu.uni.miskolc.iit.spp.core.model.Author;
import hu.uni.miskolc.iit.spp.core.model.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;

public abstract class AbstractScientificPaperBuilder implements ScientificPaperBuilder {

	public final ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException {
		return build(new File(sourceFilePath));
	}
	
	public final ScientificPaper build(File paper) throws NotSupportedFileExtensionException {
		checkFileExtension(paper);
		String title = extractTitle(paper);
		String paperAbstract = extractAbstarct(paper);
		List<String> keywords = extractKeywords(paper);
		List<Author> authors = extractAuthors(paper);
		File paperPDF = generatePDF(paper);
		return new ScientificPaper(title, paperAbstract, keywords, authors, paperPDF);
	}

	protected abstract void checkFileExtension(File paper) throws NotSupportedFileExtensionException;
	protected abstract String extractTitle(File paper);
	protected abstract String extractAbstarct(File paper);
	protected abstract List<String> extractKeywords(File paper);
	protected abstract List<Author> extractAuthors(File paper);
	protected abstract File generatePDF(File paper);
}
