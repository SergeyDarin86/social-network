package ru.skillbox.diplom.group40.social.network.impl.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventNotificationRepository extends BaseRepository<EventNotification> {
    List<EventNotification> findAllByReceiverIdAndStatusIs(UUID uuid, Status status);
    int countByReceiverIdAndStatusIs(UUID uuid, Status status);
    EventNotification findTopByOrderBySentTimeDesc();

//    Select * from table where date IN (SELECT MAX(`date`) as `time` FROM `table`)
    @Query(value =
            "Select * from event_notification where sent_time IN (SELECT MAX(`sent_time`) FROM event_notification  as sent_time)"
          , nativeQuery = true)
    EventNotification findMaxDate();

    @Query(value =
            "SELECT MAX (sent_time) AS 'maxi' FROM event_notification;", nativeQuery = true)
    ZonedDateTime findMaxDate2();

    @Query(value =
            "SELECT  greatest(sent_time) AS 'maxi' FROM event_notification;", nativeQuery = true)
    ZonedDateTime findMaxDate3();
}
