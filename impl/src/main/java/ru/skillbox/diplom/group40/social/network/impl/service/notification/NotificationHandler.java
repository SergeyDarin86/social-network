package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;

import java.util.List;

public interface NotificationHandler {

    List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO);

    Type myType();

}

