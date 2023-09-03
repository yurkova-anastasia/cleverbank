package by.yurkova.cleverbank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a Bank entity with properties such as name and active status.
 * Extends the BaseEntity class to inherit the 'id' field as a unique identifier.
 *
 * @author Yurkova Anastacia
 */
//@Getter
//@Setter
//@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bank extends BaseEntity {

    private String name;
    private Boolean active;
}
