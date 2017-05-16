package pl.rys.core.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.rys.core.entity.BaseEntity;
import pl.rys.core.entity.IFilter;

public interface AbstractService<T extends BaseEntity<S>, S extends Serializable> {
	public T save(T e);

	public T find(S id);

	public T update(T e);

	public void delete(S id);

	public List<T> list(Pageable pageable, List<IFilter> filters);

	public Long count();

	T newInstance();
}
