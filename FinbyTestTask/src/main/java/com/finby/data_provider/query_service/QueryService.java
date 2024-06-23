package com.finby.data_provider.query_service;

import com.finby.data_provider.DataProviderBase;
import com.finby.repository.CrudPagingAndSortingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class QueryService<T, ID> extends DataProviderBase<T, ID> {

    public QueryService(CrudPagingAndSortingRepository<T, ID> crudPagingAndSortingRepository) {
        super(crudPagingAndSortingRepository);
    }

    public List<T> findAll(String sort, String direction) {
        return (List<T>) crudPagingAndSortingRepository.findAll(
                Sort.by(Sort.Direction.valueOf(direction), sort)
        );
    }

    public T findById(ID id) {
        return crudPagingAndSortingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
