package com.finby.web;

import com.finby.data_provider.query_service.CategoryQueryService;
import com.finby.data_provider.command_service.CategoryCommandService;
import com.finby.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;
    private final CategoryCommandService categoryCommandService;

    @Operation(summary = "find all categories")
    @GetMapping
    public List<Category> findAll(@RequestParam String sort,
                                  @RequestParam String direction)
    {
        return categoryQueryService.findAll(sort, direction);
    }

    @Operation(summary = "find a category by ID")
    @GetMapping(path = "/{id}")
    public Category findAllById(@PathVariable Long id) {
        return categoryQueryService.findById(id);
    }

    @Operation(summary = "save a new category")
    @PostMapping
    public void save(@RequestBody Category category) {
        categoryCommandService.save(category);
    }

    @Operation(summary = "update a category")
    @PutMapping
    public void update(@RequestBody Category category) {
        categoryCommandService.update(category);
    }

    @Operation(summary = "delete a category by ID")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        categoryCommandService.delete(id);
    }
}
