package pl.rys.core.repository;

import java.io.Serializable;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AbstractRepository<T, U extends Serializable> extends PagingAndSortingRepository<T, U>, QueryDslPredicateExecutor<T> {

}
