package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedFileNames {
	MAIN("main"),
	PAPER("paper");
	
	private static String[] stringValues;
	private String stringValue;

	private SupportedFileNames(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public static String[] getStringValues() {
		stringValues = new String[SupportedFileNames.values().length];
		int i = 0;
		for(SupportedFileNames name : SupportedFileNames.values()) {
			stringValues[i] = name.getStringValue();
			i++;
		}
		return stringValues;
	}
}