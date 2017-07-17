package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedArchiveExtensions {
	ZIP("zip");
	
	private String stringValue;
	
	private SupportedArchiveExtensions(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}