package pl.rys.core.dto;

public class BaseDto<T> {
	protected T id;

	public T getId() {
		return this.id;
	}

	public void setId(T id) {
		this.id = id;
	}
}
