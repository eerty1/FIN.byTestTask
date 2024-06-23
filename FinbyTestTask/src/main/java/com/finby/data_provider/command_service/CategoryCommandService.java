package com.finby.data_provider.command_service;

import com.finby.file_manager.FileSystemCleaner;
import com.finby.file_manager.ProductImageFileManager;
import com.finby.model.Category;
import com.finby.model.image.ProductImage;
import com.finby.repository.CrudPagingAndSortingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service("categoryCommandService")
public class CategoryCommandService extends CommandService<Category, Long> implements FileSystemCleaner<Category> {
    private final ProductImageFileManager productImageFileManager;

    @Autowired
    public CategoryCommandService(CrudPagingAndSortingRepository<Category, Long> crudPagingAndSortingRepository,
                                  ProductImageFileManager productImageFileManager)
    {
        super(crudPagingAndSortingRepository);
        this.productImageFileManager = productImageFileManager;
    }

    @Override
    public void delete(Long id) {
        Category category = crudPagingAndSortingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        clearFileSystem(category);
        crudPagingAndSortingRepository.delete(category);
    }

    @Override
    public void clearFileSystem(Category category) {
        String deletionInitiator = "Category entity: " + category.getName() + " with id: " + category.getId() + " couldn't delete products' photos";

        Set<ProductImage> images = category.getProducts()
                .stream()
                .flatMap(product -> product.getProductImages().stream())
                .collect(Collectors.toSet());

        productImageFileManager.deleteFiles(productImageFileManager.extractFilePaths(images, ProductImage::getPath), deletionInitiator);
        productImageFileManager.deleteFiles(productImageFileManager.extractFilePaths(images, ProductImage::getProcessedImagePath), deletionInitiator);
    }
}
