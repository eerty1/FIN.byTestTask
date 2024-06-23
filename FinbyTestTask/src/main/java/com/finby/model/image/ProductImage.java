package com.finby.model.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProductImage extends Image {

    @JsonProperty("processed_image_path")
    private String processedImagePath;

    @Transient
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private byte[] processedImageBytes;

    public ProductImage(String path) {
        super(path);
    }
}
