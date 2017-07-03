package hu.uni.miskolc.iit.spp.core.model.exception;

public class WantedFileNotExistsException extends Exception {

	private static final long serialVersionUID = -4603371759608851092L;

	public WantedFileNotExistsException() {
	}

	public WantedFileNotExistsException(String message) {
		super(message);
	}
}
