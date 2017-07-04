package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedFileExtensions {
	TEX("tex");
	
	private String stringValue;

	private SupportedFileExtensions(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}