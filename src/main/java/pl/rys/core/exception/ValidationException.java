package pl.rys.core.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.rys.core.entity.BaseEntity;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RestException {

	private static final long serialVersionUID = 7766509988640019L;

	public static <T extends BaseEntity<U>, U> ValidationException create(Set<ConstraintViolation<T>> errors) {
		return new ValidationException(createMessage(errors));
	}

	public static <T extends BaseEntity<U>, U> String createMessage(Set<ConstraintViolation<T>> errors) {
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<T> error : errors) {
			sb.append(error.getPropertyPath());
			sb.append(": ");
			sb.append(error.getMessage());
			sb.append(", ");
		}
		sb.replace(sb.length() - 2, sb.length(), "");
		return sb.toString();
	}

	public ValidationException(String message) {
		super(message);
	}

}
