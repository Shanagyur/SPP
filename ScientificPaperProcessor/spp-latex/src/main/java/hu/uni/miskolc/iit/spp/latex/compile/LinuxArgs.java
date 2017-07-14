package hu.uni.miskolc.iit.spp.latex.compile;

public enum LinuxArgs {
	COMPILER("pdflatex "),
	OUTPUT("-output-directory=");
	
	private String argument;
	
	private LinuxArgs(String argument) {
		this.argument = argument;
	}

	public String getArgument() {
		return argument;
	}
}