package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class,
        CommentService.class,
        NotificationSettingsService.class
})
public class CommentCommentHandlerTest {
    @InjectMocks
    private CommentCommentHandler commentCommentHandler;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private CommentService commentService;
    @Mock
    NotificationSettingsService notificationSettingsService;
    @Mock
    private NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    @Before
    public void setUp() {
        commentCommentHandler = new CommentCommentHandler(
                commentService,
                new NotificationSettingsService(settingsRepository),
                Mappers.getMapper(NotificationsMapper.class));
    }

    @Test
    @DisplayName("Return List Event Notification")
    void getEventNotificationList() {

        commentCommentHandler = getNewCommentCommentHandler();

        UUID commentUUID = UUID.randomUUID();
        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Тест сообщение";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setContent(content);
        notificationDTO.setNotificationType(Type.COMMENT_COMMENT);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(receiverRandomUUID);

        Comment comment = new Comment();
        comment.setParentId(commentUUID);

        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorId(receiverRandomUUID);

        when(commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(),
                notificationDTO.getSentTime().toLocalDateTime())).thenReturn(comment);

        when(commentService.get(comment.getParentId())).thenReturn(commentDto);

        when(settingsRepository.findByAccountId(receiverRandomUUID)).thenReturn(settings);

        List<EventNotification> listEventNotifications = commentCommentHandler.getEventNotificationList(notificationDTO);

        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.COMMENT_COMMENT);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("content", content);
        }

        assertThat(listEventNotifications.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Type isEquals")
    void getType() {
        Assertions.assertEquals(commentCommentHandler.myType(), Type.COMMENT_COMMENT);
    }

    private CommentCommentHandler getNewCommentCommentHandler() {
        CommentCommentHandler commentCommentHandler = new CommentCommentHandler(
                commentService,
                new NotificationSettingsService(settingsRepository),
                Mappers.getMapper(NotificationsMapper.class));
        return commentCommentHandler;
    }
}
