package com.finby.api_client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finby.exception.ImagePersistenceException;
import com.finby.file_manager.ProductImageFileManager;
import com.finby.model.removebg_api.ErrorRoot;
import com.finby.model.image.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Component
@Slf4j
public class RemoveBgApiClient implements ApiClient<Set<ProductImage>, Void> {
    private static final String X_API_KEY = "X-Api-Key";
    private static final String IMAGE_PERSISTENCE_EXCEPTION_MESSAGE = "Couldn't remove image background";
    private static final int MAX_IN_MEMORY_SIZE = 16 * 1024 * 1024;
    private final WebClient.Builder webClientBuilder;
    private final String uri;
    private final String xApiKeyValue;

    @Autowired
    public RemoveBgApiClient(WebClient.Builder webClientBuilder,
                             @Value("${remove-bg-api.api-url}") String uri,
                             @Value("${remove-bg-api.x-api-key}") String xApiKeyValue)
    {
        this.webClientBuilder = webClientBuilder;
        this.uri = uri;
        this.xApiKeyValue = xApiKeyValue;
    }
    @Override
    public Void sendRequest(Set<ProductImage> productImages) {

        /*
            The whole "webClientBuilder.build()" could be extracted to a separate method, but I decided not to do it,
            because it is the exact part of the method that is responsible for API consumption and I believe it logically belongs here.

            The "Flux.fromIterable" is just a wrapper to perform several requests, that is why it also here.

            The other parts of the stream (exchangeStrategies(), body(), onStatus(), map()) were extracted for the sake of simplicity of the main method.

            Also, I know, that returning "null" from a method is not a good practice, but in this case it is the only possible solution.
            I can't make sendRequest() return void, since it will break the whole hierarchy of ApiClient,
            which takes into account existence of children in the future, that need to return something from method.
        */

        Flux.fromIterable(productImages)
                .flatMap(image -> {
                            if (!image.getPath().contains(ProductImageFileManager.NO_FILE_PLACEHOLDER)) {
                                return webClientBuilder
                                        .exchangeStrategies(setMaxInMemorySize())
                                        .build()
                                        .post()
                                        .uri(uri)
                                        .header(X_API_KEY, xApiKeyValue)
                                        .contentType(MULTIPART_FORM_DATA)
                                        .body(BodyInserters.fromMultipartData(buildMultipartBody(image)))
                                        .retrieve()
                                        .onStatus(HttpStatusCode::isError, handleErrorResponse())
                                        .bodyToMono(byte[].class)
                                        .onErrorResume(e -> Mono.just(new byte[0]))
                                        .map(populateImage(image));
                            } else {
                                image.setProcessedImagePath(image.getPath());
                                return Mono.just(Void.class);
                            }
                        }
                ).blockLast();
        return null;
    }

    private ExchangeStrategies setMaxInMemorySize() {
        return ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .build();
    }

    private MultiValueMap<String, HttpEntity<?>> buildMultipartBody(ProductImage productImage) {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image_file", new FileSystemResource(new File(productImage.getPath())));
        multipartBodyBuilder.part("size", "auto");
        return multipartBodyBuilder.build();
    }

    private Function<ClientResponse, Mono<? extends Throwable>> handleErrorResponse() {
        return response -> response.bodyToMono(ErrorRoot.class)
                .map(errorRoot -> {
                    try {
                        log.error(uri + "request failed with status: " + response.statusCode().value() + ". " +
                                "Response message: " + new ObjectMapper().writeValueAsString(errorRoot));
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return new ImagePersistenceException(IMAGE_PERSISTENCE_EXCEPTION_MESSAGE);
                    }
                    return new ImagePersistenceException(IMAGE_PERSISTENCE_EXCEPTION_MESSAGE);
                });
    }

    private Function<? super byte[], ? extends Mono<Class<Void>>> populateImage(ProductImage productImage) {
        return imageByte -> {
            productImage.setProcessedImageBytes(imageByte);
            return Mono.just(Void.class);
        };
    }
}
