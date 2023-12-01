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
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class
})
public class FriendRequestHandlerTest {
    @Mock
    NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    @Test
    @DisplayName("Return List Event Notification")
    void getEventNotificationList() {

        FriendRequestHandler friendRequestHandler = getNewFriendRequestHandler();

        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setReceiverId(receiverRandomUUID);
        notificationDTO.setNotificationType(Type.FRIEND_REQUEST);
        notificationDTO.setSentTime(zonedDateTime);

        when(notificationSettingsService.isNotificationTypeEnables(notificationDTO)).thenReturn(true);

        List<EventNotification> listEventNotifications = friendRequestHandler.getEventNotificationList(notificationDTO);


        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.FRIEND_REQUEST);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
        }

    }

    @Test
    @DisplayName("Type isEquals")
    void getType() {
        Assertions.assertEquals(getNewFriendRequestHandler().myType(), Type.FRIEND_REQUEST);}

    private FriendRequestHandler getNewFriendRequestHandler() {
        FriendRequestHandler friendRequestHandler = new FriendRequestHandler(notificationSettingsService, notificationsMapper);
        return friendRequestHandler;
    }
}
