package com.finby.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface CrudPagingAndSortingRepository<T, ID> extends ListCrudRepository<T, ID>, PagingAndSortingRepository<T, ID> {

}