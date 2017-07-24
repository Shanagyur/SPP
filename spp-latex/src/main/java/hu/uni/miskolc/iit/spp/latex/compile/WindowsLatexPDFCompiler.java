package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.File;

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
	protected String command4Terminal(File texFile, File destinationDir) {
		String fullCommand = 	this.compilerArg +
								this.includeDirArg + 
								texFile.getParentFile().getAbsolutePath() +
								this.outputDirArg + 
								destinationDir.getAbsolutePath() + " " + 
								texFile.getName();
		return fullCommand;
	}
}