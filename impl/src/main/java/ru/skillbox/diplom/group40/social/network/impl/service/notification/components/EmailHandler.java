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
public class EmailHandler implements NotificationHandler {
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;
    private static final String SEND_EMAIL_MESSAGE = "Вам на почту отправлена ссылка для восстановления пароля";

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("EmailHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        notificationDTO.setReceiverId(notificationDTO.getAuthorId());
        notificationDTO.setContent(SEND_EMAIL_MESSAGE);

        if (notificationSettingsService.isNotificationTypeEnables(notificationDTO)) {
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO));
        }
        log.info("EmailHandler: getEventNotificationList(_): Получен List listEventNotifications: {} " +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.SEND_EMAIL_MESSAGE;
    }
}
