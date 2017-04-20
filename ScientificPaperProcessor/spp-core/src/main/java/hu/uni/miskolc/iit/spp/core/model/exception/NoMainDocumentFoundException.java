package hu.uni.miskolc.iit.spp.core.model.exception;

public class NoMainDocumentFoundException extends ConversionToPDFException {

	private static final long serialVersionUID = -2827758543948052572L;

	public NoMainDocumentFoundException() {
	}

	public NoMainDocumentFoundException(String message) {
		super(message);
	}
}