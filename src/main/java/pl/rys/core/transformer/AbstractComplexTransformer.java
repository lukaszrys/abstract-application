package pl.rys.core.transformer;

import java.util.List;

import pl.rys.core.dto.BaseDto;
import pl.rys.core.entity.BaseEntity;
import pl.rys.core.transformer.criteria.ITransformerCriteria;

public interface AbstractComplexTransformer<T extends BaseEntity<U>, U, V extends BaseDto<U>, C extends ITransformerCriteria> {

	List<V> transform(List<T> list, C criteria);

	V transform(T object, C criteria);

	T transform(T current, V object, C criteria);

}
