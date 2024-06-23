package com.finby.repository;

import com.finby.model.Brand;
import org.springframework.stereotype.Repository;

@Repository("brandRepository")
public interface BrandRepository extends CrudPagingAndSortingRepository<Brand, Long> {
}
