package pl.rys.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RestException {

	private static final long serialVersionUID = 2944993063802540725L;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String entityName, Object id) {
		super("Not found '" + entityName.toLowerCase() + "' with id " + id);
	}

}
