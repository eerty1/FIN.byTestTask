package com.finby.data_provider.command_service.fileable;

import com.finby.data_provider.command_service.CommandService;
import com.finby.file_manager.Directory;
import com.finby.file_manager.FileSystemCleaner;
import com.finby.file_manager.ProductImageFileManager;
import com.finby.repository.CrudPagingAndSortingRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class CommandServiceFileable<T, ID> extends CommandService<T, ID> implements FileSystemCleaner<T> {
    protected final ProductImageFileManager productImageFileManager;

    public CommandServiceFileable(CrudPagingAndSortingRepository<T, ID> crudPagingAndSortingRepository, ProductImageFileManager productImageFileManager) {
        super(crudPagingAndSortingRepository);
        this.productImageFileManager = productImageFileManager;
    }

    public abstract void save(T entity, List<MultipartFile> files, Directory directory);

    public abstract void update(T entity, List<MultipartFile> files, Directory directory);

    protected abstract void saveFiles(T entity, List<MultipartFile> files, Directory directory);

    protected abstract void removeOutdatedFiles(T entity);
}
