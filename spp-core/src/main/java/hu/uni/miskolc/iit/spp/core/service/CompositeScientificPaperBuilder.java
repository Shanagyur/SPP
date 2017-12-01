package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.UseableBuilderNotExistingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompositeScientificPaperBuilder implements ScientificPaperBuilder {

	private static Logger LOG = LogManager.getLogger(CompositeScientificPaperBuilder.class);
	
	private Collection<AbstractScientificPaperBuilder> builders;

	public CompositeScientificPaperBuilder(Collection<AbstractScientificPaperBuilder> builders) {
		this.builders = builders;
	}

	@Override
	public ScientificPaper build(String sourceFilePath) throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		for(ScientificPaperBuilder builder : builders) {
			ScientificPaper paper = builder.build(sourceFilePath);
			
			return paper;
		}
		LOG.fatal("Throw UseableBuilderNotExistingException without message.");
		throw new UseableBuilderNotExistingException();
	}

	@Override
	public ScientificPaper build(File paper) throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException {
		for(ScientificPaperBuilder builder : builders) {
			ScientificPaper result = builder.build(paper);
			
			return result;
		}
		LOG.fatal("Throw UseableBuilderNotExistingException without message.");
		throw new UseableBuilderNotExistingException();
	}
}