package hu.uni.miskolc.iit.spp.core.service;

import java.io.File;

import hu.uni.miskolc.iit.spp.core.model.NotSupportedFileExtensionException;
import hu.uni.miskolc.iit.spp.core.model.ScientificPaper;

public interface ScientificPaperBuilder {
	ScientificPaper build(String sourceFilePath) throws NotSupportedFileExtensionException;
	
	ScientificPaper build(File paper) throws NotSupportedFileExtensionException;
}
