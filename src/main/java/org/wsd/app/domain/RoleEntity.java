package org.wsd.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.wsd.app.domain.listener.AuditEntityListener;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditEntityListener.class)
public class RoleEntity extends AbstractAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    private String name;
    @ManyToOne(targetEntity = UserEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private UserEntity users;
    @Version
    private int version;
}