package com.finby.repository;

import com.finby.model.Category;
import org.springframework.stereotype.Repository;

@Repository("categoryRepository")
public interface CategoryRepository extends CrudPagingAndSortingRepository<Category, Long> {
}
