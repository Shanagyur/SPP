package hu.uni.miskolc.iit.spp.latex.compile;

import java.io.File;

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
	protected String command4Terminal(File texFile, File destinationDir) {
		String fullCommand =	this.compilerArg +
								this.outputDirArg + 
								destinationDir.getAbsolutePath() + " " + 
								texFile.getAbsolutePath();
		return fullCommand;
	}
}