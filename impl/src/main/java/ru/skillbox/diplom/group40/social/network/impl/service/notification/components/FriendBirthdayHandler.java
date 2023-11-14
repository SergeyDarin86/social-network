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
public class FriendBirthdayHandler implements NotificationHandler {
    private final FriendService friendService;
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;
    private static final String AUTHOR_BIRTHDAY = " Поздравляем Вас!";

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("FriendBirthdayHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId()));
        log.info("FriendBirthdayHandler: getEventNotificationList(_): Add List<UUID> allFriends: {}", allFriends);

        for(UUID accountId : allFriends) {
            if (notificationSettingsService.isNotificationTypeEnables(accountId, notificationDTO.getNotificationType())) {
                listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO, accountId));
            }
        }

        log.info("FriendBirthdayHandler: getEventNotificationList(_): Получен List listEventNotifications: {} " +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.FRIEND_BIRTHDAY;
    }

    private void addAuthor(List<EventNotification> listEventNotifications, NotificationDTO notificationDTO) {
        if (notificationSettingsService.isNotificationTypeEnables(notificationDTO.getAuthorId(), notificationDTO.getNotificationType())) {
            notificationDTO.setContent(AUTHOR_BIRTHDAY);
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO, notificationDTO.getAuthorId()));
        }
        log.info("CommentCommentHandler: addAuthor(_): Получен List с автором listEventNotifications: {}" +
                "для дополненной контентом NotificationDTO : {}", listEventNotifications, notificationDTO);
    }
}
