package com.finby.file_manager;

/**
    This interface can seem redundant, because FileManager hierarchy already exists,
    but FileSystemCleaner has different purpose.

    FileManager is responsible for IO operations with files,
    like saving them on the machine, while FileSystemCleaner's task
    is to initiate the whole process of file deletion.

    For example: a category is deleted and all the product images have to be deleted too.
    All the steps of this process are performed by this interface, like finding a category,
    its products, collecting paths to images etc.
*/

@FunctionalInterface
public interface FileSystemCleaner<T> {
    void clearFileSystem(T entity);
}
