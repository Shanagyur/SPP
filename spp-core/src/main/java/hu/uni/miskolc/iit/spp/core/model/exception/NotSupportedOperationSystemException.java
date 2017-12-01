package hu.uni.miskolc.iit.spp.core.model.exception;

public class NotSupportedOperationSystemException extends Exception {

	private static final long serialVersionUID = 4283212490567066418L;

	public NotSupportedOperationSystemException() {
	}

	public NotSupportedOperationSystemException(String message) {
		super(message);
	}
}