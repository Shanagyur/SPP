package hu.uni.miskolc.iit.spp.latex.compile;

import hu.uni.miskolc.iit.spp.core.model.SupportedOperationSystems;
import hu.uni.miskolc.iit.spp.core.model.exception.NotSupportedOperationSystemException;

public class LatexCompilerFactory {
	
	private String osName;

	public LatexCompilerFactory(String osName) {
		this.osName = osName;
	}

	public Latex2PDFCompiler createLatexPDFCompiler() throws NotSupportedOperationSystemException {
		if(isSupportedOS()) {
			if(isLinux()) {
				return new LinuxLatexPDFCompiler();
			}
			if(isWindows()) {
				return new WindowsLatexPDFCompiler();
			}
		}
		throw new NotSupportedOperationSystemException("Could not create compiler, because this operation system is not supported.");
	}
	
	private boolean isSupportedOS() {
		for(SupportedOperationSystems system : SupportedOperationSystems.values()) {
			if(osName.toLowerCase().contains(system.getStringValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean isLinux() {
		return osName.toLowerCase().contains(SupportedOperationSystems.LINUX.getStringValue());
	}
	
	private boolean isWindows() {
		return osName.toLowerCase().contains(SupportedOperationSystems.WINDOWS.getStringValue());
	}
}