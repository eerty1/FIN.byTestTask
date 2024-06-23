package com.finby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class SpecialFeatures {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "special_features_seq"
    )
    @SequenceGenerator(
            name="special_features_seq",
            sequenceName="special_features_seq"
    )
    private Long id;

    private String name;
}
