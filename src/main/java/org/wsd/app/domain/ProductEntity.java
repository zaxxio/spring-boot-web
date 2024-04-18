package org.wsd.app.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends AbstractAuditableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private Double price;
}
