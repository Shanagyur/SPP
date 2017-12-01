package hu.uni.miskolc.iit.spp.core.model;

public enum SupportedCompileableFileExtensions {
	TEX("tex"),
	DOC("doc"),
	DOCX("docx");
	
	private String stringValue;

	private SupportedCompileableFileExtensions(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
}