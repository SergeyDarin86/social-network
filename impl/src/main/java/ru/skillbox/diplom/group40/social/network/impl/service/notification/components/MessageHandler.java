package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.MessageService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler implements NotificationHandler {
    private MessageService messageService;
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("MessageHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        Message message = messageService.getMessage(notificationDTO.getAuthorId());
        notificationDTO.setAuthorId(message.getConversationPartner1());
        notificationDTO.setReceiverId(message.getConversationPartner2());

        log.info("MessageHandler: getEventNotificationList(_) получен UUID партнера сообщения: {}," +
                "для NotificationDTO : {}", notificationDTO.getReceiverId(), notificationDTO);

        if (notificationSettingsService.isNotificationTypeEnables(notificationDTO)) {
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO));
        }
        log.info("MessageHandler: getEventNotificationList(_):  Получен List listEventNotifications: {}" +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.MESSAGE;
    }
}
