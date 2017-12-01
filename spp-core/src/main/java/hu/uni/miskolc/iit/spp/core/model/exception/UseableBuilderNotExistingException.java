package hu.uni.miskolc.iit.spp.core.model.exception;

public class UseableBuilderNotExistingException extends Exception {

	private static final long serialVersionUID = 8300046474857005178L;

	public UseableBuilderNotExistingException() {
	}

	public UseableBuilderNotExistingException(String message) {
		super(message);
	}
}