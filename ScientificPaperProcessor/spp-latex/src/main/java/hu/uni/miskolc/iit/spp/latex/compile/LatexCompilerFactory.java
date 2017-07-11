package hu.uni.miskolc.iit.spp.latex.compile;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;
import hu.uni.miskolc.iit.spp.latex.investigations.SubmissionChecker;

public class LatexCompilerFactory {
	
	private static final String OS_NAME = "os.name";

	public LatexCompilerFactory() {
	}

	public Latex2PDFCompiler createLatexPDFCompiler() throws NotSupportedOperationSystemException {
		if(SubmissionChecker.isSupportedOS(System.getProperty(OS_NAME))) {
			if(isLinux()) {
				return new LinuxLatexPDFCompiler();
			}
			if(isWindows()) {
				return new WindowsLatexPDFCompiler();
			}
		}
		throw new NotSupportedOperationSystemException("Could not create compiler, because this operation system is not supported.");
	}

	private boolean isLinux() {
		return isThisOS(SupportedOperationSystems.LINUX);
	}
	
	private boolean isWindows() {
		return isThisOS(SupportedOperationSystems.WINDOWS);
	}
	
	private boolean isThisOS(SupportedOperationSystems system) {
		return System.getProperty(OS_NAME).toLowerCase().contains(system.getStringValue());
	}
}
