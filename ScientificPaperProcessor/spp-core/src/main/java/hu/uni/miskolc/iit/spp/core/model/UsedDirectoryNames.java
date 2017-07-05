package hu.uni.miskolc.iit.spp.core.model;

public enum UsedDirectoryNames {
	DIR_FOR_EXTRACT_FILES("targetDir"),
	DIR_FOR_PDF_FILE("generatedDir");
	
	private String stringValue;

	private UsedDirectoryNames(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}