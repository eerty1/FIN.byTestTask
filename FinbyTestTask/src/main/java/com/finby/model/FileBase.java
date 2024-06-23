package com.finby.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public abstract class FileBase {

    /*
        @GeneratedValue(strategy = GenerationType.UUID) is not used here,
        because it generates ID right before flushing the entity in the database.

        Business logic needs ID to be generated together with an instance,
        otherwise absence of ID ruins equals() and hashCode(),
        removing all the entities with no-image-placeholder.png in path from Set<> in Product.
    */

    @Id
    private UUID id = UUID.randomUUID();
    private String path;
    public FileBase(String path) {
        this.path = path;
    }
}
