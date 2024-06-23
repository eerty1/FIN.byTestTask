package com.finby.data_provider.query_service;

import com.finby.model.Category;
import com.finby.repository.CrudPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("categoryQueryService")
public class CategoryQueryService extends QueryService<Category, Long> {

    @Autowired
    public CategoryQueryService(CrudPagingAndSortingRepository<Category, Long> crudPagingAndSortingRepository) {
        super(crudPagingAndSortingRepository);
    }
}
