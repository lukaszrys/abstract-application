package pl.rys.core.controller.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.TypeUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import pl.rys.core.entity.Filter;
import pl.rys.core.entity.FilterOperator;
import pl.rys.core.entity.IFilter;
import pl.rys.core.exception.ValidationException;

public class IFilterArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Type parameterType = parameter.getGenericParameterType();

		return TypeUtils.isAssignable(parameterType, new ParameterizedTypeReference<List<IFilter>>() {
		}.getType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String[] parameterValues = webRequest.getParameterValues(parameter.getParameterName());
		if (parameterValues == null || parameterValues.length == 0) {
			return new ArrayList<>();
		}
		return createFilters(Arrays.asList(parameterValues));
	}

	private List<IFilter> createFilters(List<String> filtersRaw) {
		List<IFilter> filters = new ArrayList<IFilter>();
		if (filtersRaw != null) {
			for (String filter : filtersRaw) {
				String[] filterParts = filter.split("_");
				if (filterParts.length != 3 && filterParts.length != 2) {
					throw new ValidationException("Invalid filters constraint");
				}

				filters.add(new Filter(filterParts[0], FilterOperator.valueOf(filterParts[1]),
						filterParts.length == 2 ? "" : filterParts[2]));
			}
		}
		return filters;
	}

}
