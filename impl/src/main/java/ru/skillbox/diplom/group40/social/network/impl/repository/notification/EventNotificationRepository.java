package ru.skillbox.diplom.group40.social.network.impl.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventNotificationRepository extends BaseRepository<EventNotification> {
    List<EventNotification> findAllByReceiverIdAndStatusIs(UUID uuid, Status status);
    int countByReceiverIdAndStatusIs(UUID uuid, Status status);
    @Query(value = "SELECT DISTINCT FIRST_VALUE(sent_time) over w\n" +
            "FROM event_notification WINDOW w as (ORDER BY sent_time DESC NULLS LAST)", nativeQuery = true)
    Timestamp findMaxDate();

    @Query(value = "SELECT sent_time FROM event_notification ORDER BY sent_time DESC NULLS LAST LIMIT 1;", nativeQuery = true)
    Optional<Timestamp> findTopDate();
}
