package com.finby.repository;

import com.finby.model.Brand;
import com.finby.model.Category;
import com.finby.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends CrudPagingAndSortingRepository<Product, Long> {
    List<Product> findAllByCategory(Category category, Sort sort);

    List<Product> findAllByBrand(Brand brand, Sort sort);

    List<Product> findAllByQuantityGreaterThan(int quantity, Sort sort);

    /*
        According to the task, the queries selecting products with min/max price and max rating have to return a single instance of the entity.
        However, I designed them to return a List<>, since several entries in the data table can meet requirements
    */

    @Query(
            """
            SELECT p
            FROM Product p
            WHERE p.rating = (SELECT MAX(p.rating) FROM Product p)
            """
    )
    List<Product>  findAllByMaxRating(Sort sort);

    @Query(
            """
            SELECT p
            FROM Product p
            WHERE p.price = (SELECT MAX(p.price) FROM Product p)
            """
    )
    List<Product> findAllByMaxPrice(Sort sort);

    @Query(
            """
            SELECT p
            FROM Product p
            WHERE p.price = (SELECT MIN(p.price) FROM Product p)
            """
    )
    List<Product> findAllByMinPrice(Sort sort);
}
