package hu.uni.miskolc.iit.spp.latex.compile;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.latex.investigations.OperationSystemTest;

public class LatexCompilerFactory {
	
	private static final String OS_NAME = "os.name";

	public LatexCompilerFactory() {
	}

	public Latex2PDFCompiler createLatexPDFCompiler() throws NotSupportedOperationSystemException {
		if(isLinux()) {
			return new LinuxLatexPDFCompiler();
		}
		if(isWindows()) {
			return new WindowsLatexPDFCompiler();
		}
		throw new NotSupportedOperationSystemException("Could not create compiler, because this operation system is not supported.");
	}

	private boolean isLinux() throws NotSupportedOperationSystemException {
		OperationSystemTest systemTest = new OperationSystemTest(System.getProperty(OS_NAME));
		if(systemTest.whichMatch() == SupportedOperationSystems.LINUX.getStringValue()) {
			return true;
		}
		return false;
	}
	
	private boolean isWindows() throws NotSupportedOperationSystemException {
		OperationSystemTest systemTest = new OperationSystemTest(System.getProperty(OS_NAME));
		if(systemTest.whichMatch() == SupportedOperationSystems.WINDOWS.getStringValue()) {
			return true;
		}
		return false;
	}
}
