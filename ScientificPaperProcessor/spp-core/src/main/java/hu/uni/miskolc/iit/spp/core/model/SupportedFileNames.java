package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedFileNames {
	MAIN("main"),
	PAPER("paper");
	
	private String stringValue;

	private SupportedFileNames(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}