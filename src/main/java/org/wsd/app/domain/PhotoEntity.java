package org.wsd.app.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Data
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PhotoEntity {
    @Id
    private Long id;
    private String name;
}
