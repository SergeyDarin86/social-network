package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostHandler implements NotificationHandler {
    private final FriendService friendService;
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        List<EventNotification> listEventNotifications = new ArrayList<>();

        log.info("PostHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);

        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId()));
        log.info("PostHandler: getEventNotificationList(_): Add List<UUID> allFriends: {}", allFriends);

        for(UUID accountId : allFriends) {
            if (notificationSettingsService.isNotificationTypeEnables(accountId, notificationDTO.getNotificationType())) {
                listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO, accountId));
            }
        }
        log.info("PostHandler: getEventNotificationList(_): Получен List listEventNotifications: {}" +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.POST;
    }

}
