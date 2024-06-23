package com.finby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finby.model.image.Image;
import com.finby.model.image.ProductImage;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
    Decided to remove availability field from the model, since we already have quantity.
    Having quantity > 0 we can assume, that the product is available.

    Weight property is string as the task demands ("weight": "200 г")
*/

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_seq"
    )
    @SequenceGenerator(
            name="product_seq",
            sequenceName="product_seq"
    )
    private Long id;

    private String name;
    private String model;
    private Integer quantity;
    private String weight;
    private Double rating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String description;
    private String color;
    private BigDecimal price;
    private String warranty;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    @JsonProperty("product_images")
    private Set<ProductImage> productImages = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonProperty("special_features")
    private Set<SpecialFeatures> specialFeatures;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @EqualsAndHashCode.Exclude
    private Brand brand;
}
