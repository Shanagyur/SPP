package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedGeneratedFileExtensions {
	PDF("pdf");
	
	private String stringValue;

	private SupportedGeneratedFileExtensions(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}