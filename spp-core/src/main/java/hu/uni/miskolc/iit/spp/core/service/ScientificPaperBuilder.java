package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NoMainDocumentFoundException;
import hu.uni.miskolc.iit.spp.core.model.exception.UseableBuilderNotExistingException;

public interface ScientificPaperBuilder {
	ScientificPaper build(String sourceFilePath) throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException;
	
	ScientificPaper build(File paper) throws NoMainDocumentFoundException, ConversionToPDFException, IOException, UseableBuilderNotExistingException;
}