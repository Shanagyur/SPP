package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.util.Collection;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;

public class CompositeScientificPaperBuilder implements ScientificPaperBuilder {

	private Collection<AbstractScientificPaperBuilder> builders;
	
	
	public CompositeScientificPaperBuilder(Collection<AbstractScientificPaperBuilder> builders) {
		this.builders = builders;
	}

	@Override
	public ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException {
		for(ScientificPaperBuilder builder : builders) {
			try {
				ScientificPaper paper = builder.build(sourceFilePath);
				return paper;
			} catch (NotSupportedFileExtensionException e) {

			}
		}
		throw new NotSupportedFileExtensionException();
	}

	@Override
	public ScientificPaper build(File paper) throws NotSupportedFileExtensionException {
		for(ScientificPaperBuilder builder : builders) {
			try {
				ScientificPaper result = builder.build(paper);
				return result;
			} catch (NotSupportedFileExtensionException e) {

			}
		}
		throw new NotSupportedFileExtensionException();
	}
}
