package com.finby.web;

import com.finby.data_provider.command_service.BrandCommandService;
import com.finby.data_provider.query_service.BrandQueryService;
import com.finby.model.Brand;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandQueryService brandQueryService;
    private final BrandCommandService brandCommandService;

    @Operation(summary = "find all brands")
    @GetMapping
    public List<Brand> findAll(@RequestParam String sort,
                               @RequestParam String direction)
    {
        return brandQueryService.findAll(sort, direction);
    }

    @Operation(summary = "find a brand by ID")
    @GetMapping(path = "/{id}")
    public Brand findAllById(@PathVariable Long id) {
        return brandQueryService.findById(id);
    }

    @Operation(summary = "save a new brand")
    @PostMapping
    public void save(@RequestBody Brand brand) {
        brandCommandService.save(brand);
    }

    @Operation(summary = "update a brand")
    @PutMapping
    public void update(@RequestBody Brand brand) {
        brandCommandService.update(brand);
    }

    @Operation(summary = "delete a brand by ID")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        brandCommandService.delete(id);
    }
}
