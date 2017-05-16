package pl.rys.core.entity;

public interface IFilter {
	Object getValue();

	FilterOperator getOperator();

	String getFieldName();
}
