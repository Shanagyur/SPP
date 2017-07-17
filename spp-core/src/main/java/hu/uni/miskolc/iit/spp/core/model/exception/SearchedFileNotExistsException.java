package hu.uni.miskolc.iit.spp.core.model.exception;

public class SearchedFileNotExistsException extends Exception {

	private static final long serialVersionUID = -4603371759608851092L;

	public SearchedFileNotExistsException() {
	}

	public SearchedFileNotExistsException(String message) {
		super(message);
	}
}
