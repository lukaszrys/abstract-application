package pl.rys.core.transformer.service;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.rys.core.dto.BaseDto;
import pl.rys.core.entity.BaseEntity;
import pl.rys.core.entity.IFilter;
import pl.rys.core.service.AbstractService;
import pl.rys.core.transformer.AbstractComplexTransformer;
import pl.rys.core.transformer.criteria.ITransformerCriteria;

@Service
@Transactional
public abstract class AbstractTransformerServiceImpl<T extends BaseEntity<U>, U extends Serializable, V extends BaseDto<U>, C extends ITransformerCriteria> {

	protected Logger logger = LoggerFactory.getLogger(AbstractTransformerServiceImpl.class);

	protected abstract AbstractService<T, U> getService();

	protected abstract AbstractComplexTransformer<T, U, V, C> getTransformer();

	protected abstract C getSaveCriteria();

	protected abstract C getSelectCriteria();

	protected abstract C getListCriteria();

	public V save(V dto) {
		T entity = getService().newInstance();
		entity = getTransformer().transform(entity, dto, getSaveCriteria());
		return getTransformer().transform(getService().save(entity), getSelectCriteria());
	}

	public V load(U id) {
		return getTransformer().transform(getService().find(id), getSelectCriteria());
	}

	public V update(V e, U id) {
		T entity = getService().find(id);
		getTransformer().transform(entity, e, getSaveCriteria());
		return getTransformer().transform(getService().save(entity), getSaveCriteria());
	}

	public void delete(V e) {
		delete(e.getId());
	}

	public void delete(U id) {
		getService().delete(id);
	}

	public List<V> list(Pageable pageable, List<IFilter> filters) {
		return getTransformer().transform(getService().list(pageable, filters), getListCriteria());
	}

	public Long count() {
		return getService().count();
	}

}
