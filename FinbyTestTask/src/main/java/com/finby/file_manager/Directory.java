package com.finby.file_manager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Directory {
    RESOURCES("../../src/main/resources/"),
    IMAGES("images/"),
    SOURCE_IMAGE("source_image/"),
    PROCESSED_IMAGE("processed_image/");
    private final String directoryName;
}