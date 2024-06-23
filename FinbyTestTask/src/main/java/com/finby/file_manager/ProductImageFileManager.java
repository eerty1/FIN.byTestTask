package com.finby.file_manager;

import com.finby.exception.ImagePersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.finby.file_manager.Directory.IMAGES;
import static com.finby.file_manager.Directory.RESOURCES;

@Slf4j
@Component
public class ProductImageFileManager extends FileManager {

    private static final String IMAGE_PERSISTENCE_EXCEPTION_MESSAGE = "Image persistence failed";

    public ProductImageFileManager() {
        NO_FILE_PLACEHOLDER = "no-image-placeholder.png";
    }

    public String saveFile(MultipartFile file, String fileStorageDirectory) {

        /*
            Was not very clear for me, what it means to store image name, that is why decided to make it as simple as possible
            and just include the name in the path after an underscore character.
            Now, if potential frontend need the fileName, regex pattern could be used to remove the UUID and the underscore
        */

        String fileName = UUID.randomUUID() + "_" + FilenameUtils.getName(file.getOriginalFilename());
        try {
            return saveFile(file.getBytes(), fileName, fileStorageDirectory);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ImagePersistenceException(IMAGE_PERSISTENCE_EXCEPTION_MESSAGE);
        }
    }

    public String saveFile(byte[] fileBytes, String fileName, String fileStorageDirectory) {
        if (isProcessable(fileBytes)) {
            String filePath = RESOURCES.getDirectoryName() + IMAGES.getDirectoryName() + fileStorageDirectory + fileName;
            File file = new File(filePath);

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(fileBytes);
                return filePath;
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ImagePersistenceException(IMAGE_PERSISTENCE_EXCEPTION_MESSAGE);
            }
        }
        return RESOURCES.getDirectoryName() + IMAGES.getDirectoryName() + NO_FILE_PLACEHOLDER;
    }

    public boolean isProcessable(byte[] fileBytes) {

        /*
            This method prevents the following files from being saved:
            1)Fully corrupted file -> file opened via notepad and all the content inside is removed
            2)Half corrupted file -> file opened via notepad and only some lines are removed
            3)Something went wrong with https://www.remove.bg/api and request failed resulting in .onErrorResume(e -> Mono.just(new byte[0]))
        */

        return fileBytes != null && fileBytes.length > 0 && !isCorrupted(fileBytes);
    }

    private boolean isCorrupted(byte[] fileBytes) {
        try {
            ImageIO.read(new ByteArrayInputStream(fileBytes));
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
