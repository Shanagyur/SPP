package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;

public interface ScientificPaperBuilder {
	ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException, ConversionToPDFException;
	
	ScientificPaper build(File paper) throws NotSupportedFileExtensionException, ConversionToPDFException;
}
