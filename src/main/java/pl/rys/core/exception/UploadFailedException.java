package pl.rys.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UploadFailedException extends RestException {

	private static final long serialVersionUID = -8869034625074246555L;

}
