package com.finby.data_provider;

import com.finby.repository.CrudPagingAndSortingRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataProviderBase<T, ID> {
    protected final CrudPagingAndSortingRepository<T, ID> crudPagingAndSortingRepository;
}
