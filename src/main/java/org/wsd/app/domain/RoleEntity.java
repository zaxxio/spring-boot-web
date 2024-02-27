package org.wsd.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.wsd.app.domain.listener.AuditEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditEntityListener.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoleEntity extends AbstractAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    private String name;
    @ManyToOne(targetEntity = UserEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private UserEntity users;
}