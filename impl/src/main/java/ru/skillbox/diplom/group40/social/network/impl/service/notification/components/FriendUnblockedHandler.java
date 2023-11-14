package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendUnblockedHandler implements NotificationHandler {
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("FriendUnblockedHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        if (notificationSettingsService.isNotificationTypeEnables(notificationDTO)) {
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO));
        }

        log.info("FriendUnblockedHandler: getEventNotificationList(_): Получен List listEventNotifications: {} " +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.FRIEND_UNBLOCKED;
    }
}
