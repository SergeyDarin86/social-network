package ru.skillbox.diplom.group40.social.network.impl.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventNotificationRepository extends BaseRepository<EventNotification> {
    List<EventNotification> findAllByReceiverIdAndStatusIs(UUID uuid, Status status);
    int countByReceiverIdAndStatusIs(UUID uuid, Status status);
}
