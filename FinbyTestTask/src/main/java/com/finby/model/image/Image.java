package com.finby.model.image;

import com.finby.model.FileBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
    Since in the further iterations of the application other File-like entities (that are not related to images at all) could appear,
    I decided to create a separate hierarchy for them.

    Moreover, multiple representations of Image could be created, that is why InheritanceType.TABLE_PER_CLASS was chosen.
    Using this inheritance type, all the inheritors and their unique properties are spread across specific tables,
    not clogging the base table with heterogeneous data
*/

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Image extends FileBase {

    public Image(String path) {
        super(path);
    }
}
