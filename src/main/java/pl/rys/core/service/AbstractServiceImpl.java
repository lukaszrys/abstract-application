package pl.rys.core.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.rys.core.entity.BaseEntity;
import pl.rys.core.entity.IFilter;
import pl.rys.core.repository.AbstractRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;

@Service
@Transactional
public abstract class AbstractServiceImpl<T extends BaseEntity<S>, S extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(AbstractServiceImpl.class);

	@Transactional
	public Long count() {
		logger.info("Counting all entries");
		return getRepository().count();
	}

	@Transactional
	public void delete(S id) {
		getRepository().delete(id);
		logger.info("Deleting entry with id: " + id.toString() + " in database.");
	}

	@Transactional
	public T find(S id) {
		logger.info("Loading entry with id: " + id.toString() + " from database.");
		return getRepository().findOne(id);
	}

	@Transactional
	public List<T> list(Pageable pageable, List<IFilter> filters) {
		return transformIterableToList(getRepository().findAll(createPredicates(filters), pageable));
	}

	public T newInstance() {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			logger.error("Unable to instantiate class: {}", e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("Unable to instantiate class: {}", e.getMessage());
		}
		return null;
	}

	@Transactional
	public T save(T e) {
		logger.info("Saving entry from " + e.getClass().getName() + " with id: " + e.getId() + " to database.");
		return getRepository().save(e);
	}

	@Transactional
	public T update(T e) {
		logger.info("Updating entity from " + e.getClass().getName() + " with id: " + e.getId() + " in database.");
		return getRepository().save(e);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected BooleanExpression createPredicateEq(IFilter filter, BooleanExpression predicate, Class<?> generatedClass, boolean notEqual) {
		SimpleExpression field = (SimpleExpression) getFieldValue(filter, generatedClass);
		Object value = determineTypeOfValue(field, filter.getValue());
		if (predicate == null) {
			predicate = notEqual ? field.eq(filter.getValue()).not() : field.eq(value);
		} else {
			predicate = predicate.and(notEqual ? field.eq(filter.getValue()).not() : field.eq(value));
		}
		return predicate;
	}

	@SuppressWarnings("unchecked")
	private Object determineTypeOfValue(SimpleExpression field, Object value) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(value, field.getType());
	}

	protected BooleanExpression createPredicateLike(IFilter filter, BooleanExpression predicate, Class<?> generatedClass) {
		StringPath field = null;
		field = (StringPath) getFieldValue(filter, generatedClass);
		String value = (String) filter.getValue();
		if (predicate == null) {
			predicate = field.toLowerCase().like("%" + value.toLowerCase() + "%");
		} else {
			predicate = predicate.and(field.toLowerCase().like("%" + value + "%"));
		}
		return predicate;
	}

	protected Predicate createPredicates(List<IFilter> filters) {
		Class<?> generatedClass = findGeneratedQClass();
		BooleanExpression ex = null;
		for (IFilter filter : filters) {
			switch (filter.getOperator()) {
			case EQ:
				ex = createPredicateEq(filter, ex, generatedClass, false);
				break;
			case LIKE:
				ex = createPredicateLike(filter, ex, generatedClass);
				break;
			case NEQ:
				ex = createPredicateEq(filter, ex, generatedClass, true);
				break;
			default:
				break;

			}
		}
		return ex;
	}

	@SuppressWarnings("rawtypes")
	protected Class<? extends EntityPathBase> findGeneratedQClass() {
		T clazz = newInstance();
		Reflections reflections = new Reflections(clazz.getClass().getPackage().getName());
		Set<Class<? extends EntityPathBase>> classes = reflections.getSubTypesOf(EntityPathBase.class);
		for (Class<? extends EntityPathBase> qclazz : classes) {
			if (qclazz.getSimpleName().contains(clazz.getClass().getSimpleName())) {
				return qclazz;
			}
		}
		return null;
	}

	protected Object getFieldValue(IFilter filter, Class<?> generatedClass) {
		Object field = null;
		String fieldName = filter.getFieldName();
		String fieldValue = filter.getFieldName();
		try {
			if (fieldName.contains(".")) {
				String[] split = fieldName.split("\\.");
				fieldValue = split[0];
				fieldName = split[1];
			}
			field = generatedClass.getField(fieldName).get(
					generatedClass.getDeclaredConstructor(String.class).newInstance(fieldValue));
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("Couldn't construct new instance of generatedClass  " + generatedClass.getSimpleName());
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error("Couldn't find or access field with name " + fieldName, e);
		}
		return field;
	}

	protected abstract AbstractRepository<T, S> getRepository();

	protected List<T> transformIterableToList(Iterable<T> iterable) {
		List<T> list = new ArrayList<T>();
		for (T iter : iterable) {
			list.add(iter);
		}
		return list;
	}
}
