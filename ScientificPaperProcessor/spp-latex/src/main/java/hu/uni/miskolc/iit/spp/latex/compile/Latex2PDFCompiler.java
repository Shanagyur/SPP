package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.File;
import java.io.IOException;

import hu.uni.miskolc.iit.spp.core.model.exception.ConversionToPDFException;
import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.operations.FileOperations;

public abstract class Latex2PDFCompiler {

	protected String compilerArg;
	protected String outputDirArg;
	
	protected Latex2PDFCompiler() {
		init();
	}
	
	public File generatePDFFile(File sourceDir, File destinationDir) throws ConversionToPDFException {
		try {
			compile(sourceDir, destinationDir);
			return FileOperations.findPDFFile(destinationDir);
		} catch (SearchedFileNotExistsException e) {
			throw new ConversionToPDFException(e.getMessage());
		} catch (IOException e) {
			throw new ConversionToPDFException(e.getMessage());
		}
	}
	
	protected abstract void init();
	
	protected abstract void compile(File sourceDir, File destinationDir) throws SearchedFileNotExistsException, IOException;
	
	protected abstract String commandForTerminal(File sourceDir, File destinationDir) throws SearchedFileNotExistsException;
}