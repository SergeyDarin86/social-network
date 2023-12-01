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
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class,
        PostService.class,
        CommentService.class,
})
public class PostCommentHandlerTest {

    @InjectMocks
    private PostCommentHandler postCommentHandler;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    @Before
    public void setUp() {
        postCommentHandler = new PostCommentHandler(
                postService,
                commentService,
                new NotificationSettingsService(settingsRepository),
                Mappers.getMapper(NotificationsMapper.class));
    }

    @Test
    @DisplayName("Return List Event Notification with comment")
    void getEventNotificationList() {

        postCommentHandler = getNewPostCommentHandler();

        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setReceiverId(receiverRandomUUID);
        notificationDTO.setNotificationType(Type.POST_COMMENT);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(receiverRandomUUID);

        Comment comment = new Comment();
        UUID postUUID = UUID.randomUUID();
        comment.setPostId(postUUID);

        PostDto postDto = new PostDto();
        postDto.setAuthorId(receiverRandomUUID);

        when(commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(),
                notificationDTO.getSentTime().toLocalDateTime())).thenReturn(comment);

        when(postService.get(comment.getPostId())).thenReturn(postDto);

        when(settingsRepository.findByAccountId(receiverRandomUUID)).thenReturn(settings);

        List<EventNotification> listEventNotifications = postCommentHandler.getEventNotificationList(notificationDTO);

        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.POST_COMMENT);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
        }

        assertThat(listEventNotifications.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("Type isEquals")
    void getType() {
        Assertions.assertEquals(postCommentHandler.myType(), Type.POST_COMMENT);
    }


    private PostCommentHandler getNewPostCommentHandler() {
        PostCommentHandler postCommentHandler = new PostCommentHandler(
                postService,
                commentService,
                new NotificationSettingsService(settingsRepository),
                notificationsMapper);
        return postCommentHandler;
    }
}
