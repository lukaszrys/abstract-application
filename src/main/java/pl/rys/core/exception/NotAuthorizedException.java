package pl.rys.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends RestException {

	private static final long serialVersionUID = -4160281005171265501L;

	public NotAuthorizedException(String message) {
		super(message);
	}

	public NotAuthorizedException(String entityName, Object id) {
		super("Not authorized '" + entityName.toLowerCase() + "' with id " + id);
	}

}
