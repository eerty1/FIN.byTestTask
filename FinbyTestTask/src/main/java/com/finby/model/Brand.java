package com.finby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /*
        Here GenerationType.IDENTITY is used to make id generation compatible with data.sql file (SEQUENCE is not),
        that populates data on application startup.

        In real-world applications SEQUENCE strategy is preferred, but in this project
        SEQUENCE is used only with entities, that are created manually - it avoids ids' conflicts.
    */

    private Long id;
    private String name;

    @OneToMany(mappedBy = "brand", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    @JsonIgnore
    private Set<Product> products;

    @PreRemove
    private void detachProducts() {
        products.forEach(product -> product.setBrand(null));
    }
}
