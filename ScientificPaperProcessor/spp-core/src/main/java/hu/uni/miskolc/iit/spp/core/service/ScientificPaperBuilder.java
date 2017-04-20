package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;
import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedFileExtensionException;

public interface ScientificPaperBuilder {
	ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException, ConversionToPDFException, IOException;
	
	ScientificPaper build(File paper) throws NotSupportedFileExtensionException, ConversionToPDFException, IOException;
}
