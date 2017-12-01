package hu.uni.miskolc.iit.spp.core.model.exception;

public class NotSupportedFileExtensionException extends Exception {

	private static final long serialVersionUID = -1260269304788296032L;

	public NotSupportedFileExtensionException() {
	}

	public NotSupportedFileExtensionException(String message) {
		super(message);
	}
}