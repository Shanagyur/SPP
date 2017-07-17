package hu.uni.miskolc.iit.spp.core.model.exception;

public class NotSupportedArchiveExtensionException extends NotSupportedFileExtensionException {

	private static final long serialVersionUID = -8863045212620950177L;

	public NotSupportedArchiveExtensionException() {
	}

	public NotSupportedArchiveExtensionException(String message) {
		super(message);
	}

}
