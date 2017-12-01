package hu.uni.miskolc.iit.spp.core.model.exception;

public class NotSupportedApplicationException extends Exception {

	private static final long serialVersionUID = -3226656804373276725L;

	public NotSupportedApplicationException() {
	}

	public NotSupportedApplicationException(String message) {
		super(message);
	}
}