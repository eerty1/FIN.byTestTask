package com.finby.file_manager;

import com.finby.model.FileBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class FileManager {
    public static String NO_FILE_PLACEHOLDER;

    public abstract String saveFile(MultipartFile file, String fileStorageDirectory);

    public void deleteFiles(Set<String> filePaths, String deletionInitiator) {
        filePaths.forEach(path -> {
            if (!path.contains(NO_FILE_PLACEHOLDER)) {
                if (!new File(path).delete()) {
                    log.warn(filePaths + " was not deleted. Deletion initiator: " + deletionInitiator);
                }
            }
        });
    }

    public <S extends FileBase> Set<String> extractFilePaths(Set<S> files, Function<? super S, String> mapper) {
        return files.stream().map(mapper).collect(Collectors.toSet());
    }
}
