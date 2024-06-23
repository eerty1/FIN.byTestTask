package com.finby.data_provider.query_service;

import com.finby.model.Brand;
import com.finby.model.Category;
import com.finby.model.Product;
import com.finby.repository.CrudPagingAndSortingRepository;
import com.finby.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productQueryService")
public class ProductQueryService extends QueryService<Product, Long> {
    private final ProductRepository productRepository = ((ProductRepository) crudPagingAndSortingRepository);

    @Autowired
    public ProductQueryService(CrudPagingAndSortingRepository<Product, Long> crudPagingAndSortingRepository) {
        super(crudPagingAndSortingRepository);
    }

    public List<Product> findAllByCategory(Category category, String sort, String direction) {
        return productRepository.findAllByCategory(
                category,
                Sort.by(Sort.Direction.valueOf(direction), sort)
        );
    }

    public List<Product> findAllByBrand(Brand brand, String sort, String direction) {
        return productRepository.findAllByBrand(
                brand,
                Sort.by(Sort.Direction.valueOf(direction), sort)
        );
    }

    public List<Product> findAllAvailable(String sort, String direction) {
        //a product is considered available, if its quantity is greater than 0
        return productRepository.findAllByQuantityGreaterThan(
                0,
                Sort.by(Sort.Direction.valueOf(direction), sort)
        );
    }

    public List<Product> findAllByMaxRating(String sort, String direction) {
        return productRepository.findAllByMaxRating(Sort.by(Sort.Direction.valueOf(direction), sort));
    }

    public List<Product> findAllByPriceBound(String bound, String sort, String direction) {
        return switch (bound) {
            case "upper" -> productRepository.findAllByMaxPrice(Sort.by(Sort.Direction.valueOf(direction), sort));
            case "lower" -> productRepository.findAllByMinPrice(Sort.by(Sort.Direction.valueOf(direction), sort));
            default -> throw new IllegalStateException("Unexpected value: " + bound);
        };
    }
}
