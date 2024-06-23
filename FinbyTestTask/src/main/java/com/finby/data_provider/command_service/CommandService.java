package com.finby.data_provider.command_service;

import com.finby.data_provider.DataProviderBase;
import com.finby.repository.CrudPagingAndSortingRepository;

public class CommandService<T, ID> extends DataProviderBase<T, ID> {

    public CommandService(CrudPagingAndSortingRepository<T, ID> crudPagingAndSortingRepository) {
        super(crudPagingAndSortingRepository);
    }

    public void save(T entity) {
        crudPagingAndSortingRepository.save(entity);
    }

    public void update(T entity) {
        crudPagingAndSortingRepository.save(entity);
    }

    public void delete(ID id) {
        crudPagingAndSortingRepository.deleteById(id);
    }
}
