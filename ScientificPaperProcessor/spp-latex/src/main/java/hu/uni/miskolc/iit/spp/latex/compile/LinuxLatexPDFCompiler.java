package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.operations.FileOperations;

public class LinuxLatexPDFCompiler extends Latex2PDFCompiler {

	protected LinuxLatexPDFCompiler() {
		super();
	}

	@Override
	protected void init() {
		this.compilerArg = LinuxArgs.COMPILER.getArgument();
		this.outputDirArg = LinuxArgs.OUTPUT.getArgument();
	}

	@Override
	protected void compile(File sourceDir, File destinationDir) throws SearchedFileNotExistsException, IOException {
		Process process = Runtime.getRuntime().exec(commandForTerminal(sourceDir, destinationDir));
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String string;
		while((string = reader.readLine()) != null) {
		}
	}

	@Override
	protected String commandForTerminal(File sourceDir, File destinationDir) throws SearchedFileNotExistsException {
		File mainFile = FileOperations.findTexFile(sourceDir);
		String fullCommand =	this.compilerArg + 
								this.outputDirArg + 
								destinationDir.getAbsolutePath() + " " + 
								mainFile.getAbsolutePath();
		return fullCommand;
	}
}