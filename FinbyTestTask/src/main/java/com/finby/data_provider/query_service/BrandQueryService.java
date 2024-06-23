package com.finby.data_provider.query_service;

import com.finby.model.Brand;
import com.finby.repository.CrudPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("brandQueryService")
public class BrandQueryService extends QueryService<Brand, Long> {

    @Autowired
    public BrandQueryService(CrudPagingAndSortingRepository<Brand, Long> crudPagingAndSortingRepository) {
        super(crudPagingAndSortingRepository);
    }
}
