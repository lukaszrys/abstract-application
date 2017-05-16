package pl.rys.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.rys.core.dto.BaseDto;
import pl.rys.core.entity.Filter;
import pl.rys.core.entity.FilterOperator;
import pl.rys.core.entity.IFilter;
import pl.rys.core.exception.ValidationException;
import pl.rys.core.transformer.service.AbstactTransformerService;

/**
 * Controller for operations on contact.
 * 
 * @author LRys
 *
 */
@RestController
public abstract class AbstractController<T extends BaseDto<S>, S> {

	@Autowired
	private AbstactTransformerService<S, T> transformerService = getTransformerService();

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable S id) {
		transformerService.delete(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<T> findAll(@RequestBody(required = false) Pageable pageable,
			@RequestParam(required = false) List<String> filters) {
		return transformerService.list(pageable, createFilters(filters));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public T save(@RequestBody T dto) {
		return transformerService.save(dto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public T updatePUT(@RequestBody T dto, @PathVariable S id) {
		return transformerService.update(dto, id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public T view(@PathVariable S id) {
		return transformerService.load(id);
	}

	protected abstract AbstactTransformerService<S, T> getTransformerService();

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
