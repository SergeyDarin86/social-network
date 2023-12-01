package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class, SettingsRepository.class
})
public class EmailHandlerTest {

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    NotificationSettingsService notificationSettingsService; /*= new NotificationSettingsService(settingsRepository);*/

    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    @Test
    @DisplayName("Return List Event Notification")
    void getEventNotificationList() {

        EmailHandler emailHandler = getNewEmailHandler();

        UUID randomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Вам на почту отправлена ссылка для восстановления пароля";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setNotificationType(Type.SEND_EMAIL_MESSAGE);
        notificationDTO.setSentTime(zonedDateTime);

        Settings notificationSettings = new Settings();
        notificationSettings.setAccountId(randomUUID);

        /*when(settingsRepository.findByAccountId(randomUUID)).thenReturn(notificationSettings);*/
        when(notificationSettingsService.isNotificationTypeEnables(notificationDTO)).thenReturn(true);

        List<EventNotification> listEventNotifications = emailHandler.getEventNotificationList(notificationDTO);


        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("content", content);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.SEND_EMAIL_MESSAGE);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
        }

    }

    @Test
    @DisplayName("Type isEquals")
    void getType() {Assertions.assertEquals(getNewEmailHandler().myType(), Type.SEND_EMAIL_MESSAGE);}

    private EmailHandler getNewEmailHandler() {
       EmailHandler emailHandler = new EmailHandler(notificationSettingsService, notificationsMapper);
       return emailHandler;
    }
}
