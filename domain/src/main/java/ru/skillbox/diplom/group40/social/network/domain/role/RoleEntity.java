package ru.skillbox.diplom.group40.social.network.domain.role;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;
import ru.skillbox.diplom.group40.social.network.domain.user.UserEntity;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {

    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();
}
