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
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class
})
public class PostHandlerTest {
    @Mock
    SettingsRepository settingsRepository;
    @Mock
    private FriendService friendService;
    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    @Test
    @DisplayName("Return List Event Notification")
    void getEventNotificationList() {

        PostHandler postHandler = getNewPostHandler();

        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
//        UUID receiver2RandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Тест пост";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setContent(content);
//        notificationDTO.setReceiverId(receiverRandomUUID);
        notificationDTO.setNotificationType(Type.POST);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(receiverRandomUUID);
        when(settingsRepository.findByAccountId(receiverRandomUUID)).thenReturn(settings);

        /*
        Settings settings2 = new Settings();
        settings.setAccountId(receiver2RandomUUID);
        when(settingsRepository.findByAccountId(receiver2RandomUUID)).thenReturn(settings2);
        when(settingsRepository.findByAccountId(receiver2RandomUUID)).thenReturn(settings2);
        */

        List<String> friendsList = new ArrayList<>();
        friendsList.add(receiverRandomUUID.toString());
        /** NULL на выходе*/
//        friendsList.add(receiver2RandomUUID.toString());

        when(friendService.getAllFriendsById(notificationDTO.getAuthorId())).thenReturn(friendsList);

        List<EventNotification> listEventNotifications = postHandler.getEventNotificationList(notificationDTO);

        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
//            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.POST);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("content", content);
//            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
        }

        assertThat(listEventNotifications.size()).isEqualTo(friendsList.size());
//        assertThrows(SomeException.class, () -> mockObject.someMethod());
    }

    @Test
    @DisplayName("Type isEquals")
    void getType() {Assertions.assertEquals(getNewPostHandler().myType(), Type.POST);}

    private PostHandler getNewPostHandler() {
        PostHandler postHandler = new PostHandler(
                friendService,
                new NotificationSettingsService(settingsRepository), notificationsMapper);
        return postHandler;
    }
}
