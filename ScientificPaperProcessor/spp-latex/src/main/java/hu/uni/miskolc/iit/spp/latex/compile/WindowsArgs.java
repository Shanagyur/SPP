package hu.uni.miskolc.iit.spp.latex.compile;

public enum WindowsArgs {
	COMPILER("pdflatex "),
	INCLUDE("-include-directory="),
	OUTPUT(" -output-directory=");
	
	private String argument;
	
	private WindowsArgs(String argument) {
		this.argument = argument;
	}

	public String getArgument() {
		return argument;
	}
}