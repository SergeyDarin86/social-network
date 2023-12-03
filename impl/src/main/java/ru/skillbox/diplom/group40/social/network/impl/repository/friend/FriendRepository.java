package ru.skillbox.diplom.group40.social.network.impl.repository.friend;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.*;

public interface FriendRepository extends BaseRepository<Friend> {

    Optional<Friend> findByAccountFromAndAccountTo(UUID accountFrom, UUID accountTo);

    @Modifying
    @Query(value = """
            INSERT INTO friend (id, account_from, account_to, status_code, is_deleted)
            VALUES (uuid_generate_v4(), :accountFrom, :accountTo, :statusCode, 'false')
            ON CONFLICT (account_from, account_to) 
            DO UPDATE SET status_code = :statusCode, is_deleted = 'false'""", nativeQuery = true)
    void addRelationship(@Param("accountFrom") UUID accountFrom, @Param("accountTo") UUID accountTo
            , @Param("statusCode") String statusCode);

    @Query(value = """
            SELECT account_from AS id, COUNT(account_to) AS rating 
                FROM friend
            WHERE status_code = 'FRIEND' AND account_from <> :current_user 
                AND NOT is_deleted 
                AND NOT account_from IN (SELECT account_from FROM friend WHERE  
                                            account_to = :current_user AND NOT is_deleted) 
                AND account_to IN (SELECT account_to FROM friend WHERE status_code = 'FRIEND' 
                                        AND account_from = :current_user AND NOT is_deleted)
            GROUP BY account_from
            ORDER BY COUNT(account_to) DESC LIMIT 10"""
            , nativeQuery = true)
    List<Object[]> findAllOrderedByNumberFriends(@Param("current_user") UUID id);

    List<Friend> findByAccountFromAndStatusCodeAndIsDeletedFalse(UUID id, StatusCode status);

    Integer countByAccountFromAndStatusCodeAndIsDeleted(UUID id, StatusCode status, Boolean IsDeleted);

    List<Friend> findAllByAccountToInAndAccountFromAndIsDeletedFalse(List<UUID> ids, UUID id);

    List<Friend> findByAccountToAndStatusCodeInAndIsDeletedFalse(UUID id, List<StatusCode> status);

    List<Friend> findAllByAccountToAndIsDeletedFalse(UUID id);
}
