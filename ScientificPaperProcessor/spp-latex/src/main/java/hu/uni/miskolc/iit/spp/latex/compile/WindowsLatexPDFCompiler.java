package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.uni.miskolc.iit.spp.core.model.exception.SearchedFileNotExistsException;
import hu.uni.miskolc.iit.spp.latex.fileOperations.FileSearch;

public class WindowsLatexPDFCompiler extends Latex2PDFCompiler {

	private String includeDirArg;
	
	protected WindowsLatexPDFCompiler() {
		super();
	}

	@Override
	protected void init() {
		this.compilerArg = WindowsArgs.COMPILER.getArgument();
		this.includeDirArg = WindowsArgs.INCLUDE.getArgument();
		this.outputDirArg = WindowsArgs.OUTPUT.getArgument();
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
		FileSearch file = new FileSearch(sourceDir);
		File mainFile = file.findTexFile();
		String fullCommand = 	this.compilerArg + 
								this.includeDirArg + 
								sourceDir.getAbsolutePath() + 
								this.outputDirArg + 
								destinationDir.getAbsolutePath() + " " + 
								mainFile.getName();
		return fullCommand;
	}
}