package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedOperationSystems {
	LINUX("linux"),
	WINDOWS("windows");
	
	private String stringValue;

	private SupportedOperationSystems(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}