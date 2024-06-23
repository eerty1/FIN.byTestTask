package com.finby.web;

import com.finby.data_provider.query_service.ProductQueryService;
import com.finby.data_provider.command_service.fileable.ProductCommandServiceFileable;
import com.finby.model.Brand;
import com.finby.model.Category;
import com.finby.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.finby.file_manager.Directory.SOURCE_IMAGE;


@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductCommandServiceFileable productCommandServiceFileable;

    @Operation(summary = "find all products")
    @GetMapping
    public List<Product> findAll(@RequestParam String sort,
                                 @RequestParam String direction)
    {
        return productQueryService.findAll(sort, direction);
    }

    @Operation(summary = "find a product by ID")
    @GetMapping(path = "/{id}")
    public Product findAllById(@PathVariable Long id) {
        return productQueryService.findById(id);
    }

    @Operation(summary = "find products by a category")
    @PostMapping(path = "/category")
    public List<Product> findAllByCategory(@RequestBody Category category,
                                           @RequestParam String sort,
                                           @RequestParam String direction)
    {
        return productQueryService.findAllByCategory(category, sort, direction);
    }

    @Operation(summary = "find products by a brand")
    @PostMapping(path = "/brand")
    public List<Product> findAllByBrand(@RequestBody Brand brand,
                                        @RequestParam String sort,
                                        @RequestParam String direction)
    {
        return productQueryService.findAllByBrand(brand, sort, direction);
    }

    @Operation(summary = "find available products")
    @GetMapping(path = "/availability")
    public List<Product> findAllByAvailable(@RequestParam String sort,
                                            @RequestParam String direction)
    {
        return productQueryService.findAllAvailable(sort, direction);
    }

    @Operation(
            summary = "find all products by price bound",
            description = "permitted value for bound: lower/upper"
    )
    @GetMapping(path = "/price/{bound}")
    public List<Product> findAllByPriceBound(@PathVariable String bound,
                                             @RequestParam String sort,
                                             @RequestParam String direction)
    {
        return productQueryService.findAllByPriceBound(bound, sort, direction);
    }

    @Operation(summary = "find all products with max rating")
    @GetMapping(path = "/rating")
    public List<Product> findByMaxRating(@RequestParam String sort,
                                         @RequestParam String direction)
    {
        return productQueryService.findAllByMaxRating(sort, direction);
    }

    @Operation(summary = "save a new product")
    @PostMapping
    public void save(@RequestPart Product product,
                     @RequestPart List<MultipartFile> files)
    {
        productCommandServiceFileable.save(product, files, SOURCE_IMAGE);
    }

    @Operation(summary = "update a product")
    @PutMapping
    public void update(@RequestPart Product product,
                       @RequestPart List<MultipartFile> files)
    {
        productCommandServiceFileable.update(product, files, SOURCE_IMAGE);
    }

    @Operation(summary = "delete a product by ID")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        productCommandServiceFileable.delete(id);
    }
}
