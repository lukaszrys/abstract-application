package pl.rys.core.transformer;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.rys.core.dto.BaseDto;
import pl.rys.core.entity.BaseEntity;
import pl.rys.core.transformer.criteria.ITransformerCriteria;

public abstract class AbstractComplexTransformerImpl<T extends BaseEntity<U>, U, V extends BaseDto<U>, C extends ITransformerCriteria> implements
		AbstractComplexTransformer<T, U, V, C> {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractComplexTransformerImpl.class);

	public List<V> transform(List<T> list, C criteria) {
		List<V> result = new ArrayList<V>();
		for (T object : list) {
			result.add(transform(object, criteria));
		}
		return result;
	}

	public V transform(T object, C criteria) {
		V dto = createNewDtoInstance();
		transformToDto(dto, object, criteria);
		if (object != null && dto != null) {
			dto.setId(object.getId());
		}
		return dto;
	}

	public T transform(T current, V object, C criteria) {
		transformFromDto(current, object, criteria);
		return current;
	}

	@SuppressWarnings("unchecked")
	protected V createNewDtoInstance() {
		V dto = null;
		try {
			dto = ((Class<V>) genericParam(this.getClass(), 2)).newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("Unable to create an instance of DTO", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to create an instance of DTO", e);
		}
		return dto;
	}

	protected abstract void transformFromDto(T object, V dto, C criteria);

	protected abstract void transformToDto(V dto, T object, C criteria);

	private Class<?> genericParam(Class<?> clazz, int which) {
		return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[which];
	}

}