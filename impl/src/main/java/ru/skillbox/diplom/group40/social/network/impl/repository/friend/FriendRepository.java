package ru.skillbox.diplom.group40.social.network.impl.repository.friend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.*;

@Repository
public interface FriendRepository extends BaseRepository<Friend> {
    Optional<Friend> findByAccountFromAndAccountTo(UUID accountFrom, UUID accountTo);

    @Query(value =
            "SELECT account_from AS id, COUNT(account_to) AS rating \n" +
            "FROM friend\n" +
            "WHERE status_code = 'FRIEND' AND account_from <> :current_user \n" +
            "AND NOT is_deleted \n" +
            "AND NOT account_from IN " +
            "(SELECT account_from FROM friend WHERE  " +
            "account_to = :current_user AND NOT is_deleted) \n" +
            "AND account_to IN " +
            "(SELECT account_to FROM friend WHERE status_code = 'FRIEND' " +
            "AND account_from = :current_user AND NOT is_deleted)" +
            "GROUP BY account_from \n" +
            "ORDER BY COUNT(account_to) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findAllOrderedByNumberFriends(@Param("current_user") UUID id);

    List<Friend> findByAccountFrom(UUID id);

    List<Friend> findByAccountFromAndStatusCodeAndIsDeletedFalse(UUID id, StatusCode status);

    Integer countByAccountFromAndStatusCodeAndIsDeleted(UUID id, StatusCode status, Boolean IsDeleted);

    List<Friend> findAllByAccountToInAndAccountFromAndIsDeletedFalse(List<UUID> ids, UUID id);

    List<Friend> findByAccountToAndStatusCodeInAndIsDeletedFalse(UUID id, List<StatusCode> status);
}
