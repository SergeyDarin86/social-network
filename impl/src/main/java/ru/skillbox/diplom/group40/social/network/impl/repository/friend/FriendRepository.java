package ru.skillbox.diplom.group40.social.network.impl.repository.friend;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends BaseRepository<Friend> {
    Optional<Friend> findByAccountFromAndAccountTo(UUID accountFrom, UUID accountTo);

    List<Friend> findByAccountFrom(UUID id);
}
