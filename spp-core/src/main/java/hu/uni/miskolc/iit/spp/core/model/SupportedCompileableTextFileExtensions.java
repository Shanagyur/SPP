package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedCompileableTextFileExtensions {
	TEX("tex");
	
	private String stringValue;

	private SupportedCompileableTextFileExtensions(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}