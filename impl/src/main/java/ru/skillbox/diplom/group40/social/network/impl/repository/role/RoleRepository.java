package ru.skillbox.diplom.group40.social.network.impl.repository.role;


import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {
    Optional<Role> getByRole(String role);
}
