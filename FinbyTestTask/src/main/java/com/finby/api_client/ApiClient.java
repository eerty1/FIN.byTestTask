package com.finby.api_client;

public interface ApiClient<T, R> {
    R sendRequest(T entity);
}
