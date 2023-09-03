package by.yurkova.cleverbank.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base entity class representing common properties for all entities.
 * It includes an 'id' field as a unique identifier for the entity.
 *
 * @author Yurkova Anastacia
 */
//@Getter
//@Setter
//@ToString
@NoArgsConstructor
@Data
public abstract class BaseEntity {

    private Long id;
}
