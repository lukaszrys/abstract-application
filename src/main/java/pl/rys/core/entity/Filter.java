package pl.rys.core.entity;

public class Filter implements IFilter {

	private Object value;
	private FilterOperator operator;
	private String fieldName;

	public Filter(String fieldName, FilterOperator operator, Object value) {
		this.value = value;
		this.operator = operator;
		this.fieldName = fieldName;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public FilterOperator getOperator() {
		return operator;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

}
