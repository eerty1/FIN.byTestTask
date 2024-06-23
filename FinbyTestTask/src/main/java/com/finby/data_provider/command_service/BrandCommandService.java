package com.finby.data_provider.command_service;

import com.finby.data_provider.command_service.fileable.ProductCommandServiceFileable;
import com.finby.data_provider.query_service.ProductQueryService;
import com.finby.model.Brand;
import com.finby.model.Product;
import com.finby.repository.CrudPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("brandCommandService")
public class BrandCommandService extends CommandService<Brand, Long> implements DescriptionBuilder<Brand> {
    private final ProductQueryService productQueryService;
    private final ProductCommandServiceFileable productCommandServiceFileable;

    @Autowired
    public BrandCommandService(CrudPagingAndSortingRepository<Brand, Long> crudPagingAndSortingRepository,
                               ProductQueryService productQueryService,
                               ProductCommandServiceFileable productCommandServiceFileable)
    {
        super(crudPagingAndSortingRepository);
        this.productQueryService = productQueryService;
        this.productCommandServiceFileable = productCommandServiceFileable;
    }

    @Override
    public void update(Brand brand) {
        buildDescription(brand);
        super.update(brand);
    }

    @Override
    public void buildDescription(Brand brand) {
        List<Product> productsByBrand = productQueryService.findAllByBrand(brand, "id", "ASC");
        productsByBrand.forEach(product -> {
            product.setBrand(brand);
            productCommandServiceFileable.buildDescription(product);
        });
    }
}
