package com.finby.data_provider.command_service.fileable;

import com.finby.data_provider.command_service.DescriptionBuilder;
import com.finby.file_manager.Directory;
import com.finby.file_manager.ProductImageFileManager;
import com.finby.model.image.ProductImage;
import com.finby.model.Product;
import com.finby.repository.CrudPagingAndSortingRepository;
import com.finby.api_client.RemoveBgApiClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.finby.file_manager.Directory.PROCESSED_IMAGE;

@Slf4j
@Service("productCommandServiceFileable")
public class ProductCommandServiceFileable extends CommandServiceFileable<Product, Long> implements DescriptionBuilder<Product> {
    private static final String REGEX_REMOVE_ALL_BEFORE_UNDERSCORE = "^.*(?=_)";
    private final RemoveBgApiClient removeBgApiConsumer;

    @Autowired
    public ProductCommandServiceFileable(CrudPagingAndSortingRepository<Product, Long> crudPagingAndSortingRepository,
                                         ProductImageFileManager productImageFileManager,
                                         RemoveBgApiClient removeBgApiConsumer)
    {
        super(crudPagingAndSortingRepository, productImageFileManager);
        this.removeBgApiConsumer = removeBgApiConsumer;
    }

    @Override
    public void save(Product product, List<MultipartFile> files, Directory directory) {
        saveFiles(product, files, directory);
        removeBgApiConsumer.sendRequest(product.getProductImages());
        saveProcessedImages(product);
        buildDescription(product);
        crudPagingAndSortingRepository.save(product);
    }

    @Override
    public void update(Product product, List<MultipartFile> files, Directory directory) {
        removeOutdatedFiles(product);

        //user wants to delete photos without adding new ones or simply leave everything as it was
        if (!product.getProductImages().isEmpty() && files.get(0).getSize() == 0) {
            buildDescription(product);
            crudPagingAndSortingRepository.save(product);
            return;
        }

        Set<ProductImage> preservedImages = new HashSet<>(product.getProductImages()); //find already existing, but not deleted images
        saveFiles(product, files, directory);
        product.getProductImages().removeAll(preservedImages); //exempt preservedImages from background removal

        //if (files.size() > 0) case is absent, since the situations, where user attach no photos, are handled above
        removeBgApiConsumer.sendRequest(product.getProductImages());
        saveProcessedImages(product);

        product.getProductImages().addAll(preservedImages); //return preservedImages back to the product

        buildDescription(product);
        crudPagingAndSortingRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        Product product = crudPagingAndSortingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        clearFileSystem(product);
        crudPagingAndSortingRepository.delete(product);
    }

    @Override
    protected void saveFiles(Product product, List<MultipartFile> files, Directory directory) {
        files.forEach(file ->
                product.getProductImages().add(
                        new ProductImage(productImageFileManager.saveFile(file, directory.getDirectoryName()))
                )
        );
    }

    @Override
    protected void removeOutdatedFiles(Product product) {
        Product outdatedProduct = crudPagingAndSortingRepository.findById(product.getId()).orElseThrow(EntityNotFoundException::new);
        outdatedProduct.getProductImages().removeAll(product.getProductImages());
        clearFileSystem(outdatedProduct);
    }

    protected void saveProcessedImages(Product product) {
        product.getProductImages()
                .forEach(image -> image
                        .setProcessedImagePath(
                                productImageFileManager.saveFile(
                                        image.getProcessedImageBytes(),
                                        buildProcessedFileName(image),
                                        PROCESSED_IMAGE.getDirectoryName()
                                )
                        )
                );
    }

    /*
        Of course application would work properly without this method, but just as a means of precaution,
        I decided to implement it in order to avoid any file-naming conflicts in the future.
    */
    private String buildProcessedFileName(ProductImage image) {
        return UUID.randomUUID() + FilenameUtils.getName(image.getPath().replaceAll(REGEX_REMOVE_ALL_BEFORE_UNDERSCORE, ""));
    }

    @Override
    public void clearFileSystem(Product product) {
        String deletionInitiator = "Product entity: " + product.getName() + " with id: " + product.getId() + " couldn't delete it's photos";
        productImageFileManager.deleteFiles(productImageFileManager.extractFilePaths(product.getProductImages(), ProductImage::getPath), deletionInitiator);
        productImageFileManager.deleteFiles(productImageFileManager.extractFilePaths(product.getProductImages(), ProductImage::getProcessedImagePath), deletionInitiator);
    }


    @Override
    public void buildDescription(Product product) {

        /*
            This method is how I understood assignment 1.1.
            Formed *description* precisely mimics the pattern from the document with the task
        */

        StringBuilder specialFeatures = new StringBuilder();
        product.getSpecialFeatures().forEach(specialFeature -> specialFeatures
                .append(specialFeature.getName())
                .append(", ")
        );


        product.setDescription(
                product.getName() + " " +
                product.getBrand().getName() + ", цвет: " +
                product.getColor() + "." +
                (!specialFeatures.isEmpty()
                        ? " " + specialFeatures.delete(specialFeatures.length() - 2, specialFeatures.length()) + "."
                        : ""
                )
        );
    }
}
