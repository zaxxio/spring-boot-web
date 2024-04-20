package org.wsd.app.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "products")
public class ProductEntity extends AbstractAuditableEntity implements Serializable {
    @Id
    private UUID id;
    private String name;
    private String description;
    private Double price;
}
