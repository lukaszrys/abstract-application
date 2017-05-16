package pl.rys.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestException extends RuntimeException {

	private static final long serialVersionUID = -1633061979579544869L;

	public RestException() {
		super();
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public int httpStatus() {
		ResponseStatus annotation = this.getClass().getAnnotation(ResponseStatus.class);
		return annotation == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : annotation.value().value();
	}

}
