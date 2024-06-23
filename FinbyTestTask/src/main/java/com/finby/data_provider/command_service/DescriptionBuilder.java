package com.finby.data_provider.command_service;

@FunctionalInterface
public interface DescriptionBuilder<T> {

    void buildDescription(T entity);
}
