package ru.skillbox.diplom.group40.social.network.impl.repository.user;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.user.User;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findFirstByEmail(String email);
    Optional<User> findFirstById(UUID id);
}
