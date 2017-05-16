package pl.rys.core.transformer.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.rys.core.dto.BaseDto;
import pl.rys.core.entity.IFilter;

public interface AbstactTransformerService<U, V extends BaseDto<U>> {
	public V save(V e);

	public V load(U id);

	public V update(V e, U id);

	public void delete(V e);

	public void delete(U id);

	public List<V> list(Pageable pageable, List<IFilter> filters);

	public Long count();

}
