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
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.LikeService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
        NotificationSettingsService.class,
        PostService.class,
        CommentService.class,
        NotificationSettingsService.class
})
public class LikeHandlerTest {
    @InjectMocks
    private LikeHandler likeHandler;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private LikeService likeService;
    @Mock
    NotificationSettingsService notificationSettingsService;
    @Mock
    private NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);
//    @Mock
//    private NotificationSettingsService notificationSettingsServices = new NotificationSettingsService(settingsRepository);
    private static final String SEND_LIKE_POST = "поставил LIKE на Вашу запись \"";
    private static final String SEND_LIKE_COMMENT = "поставил LIKE на Ваш комментарий \"";

    @Before
    public void setUp() {
       likeHandler = new LikeHandler(
                likeService,
                commentService,
                postService,
                new NotificationSettingsService(settingsRepository),
                Mappers.getMapper(NotificationsMapper.class));

//        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Return List Event Notification with post")
    void getEventNotificationListPost() {

        likeHandler = getNewLikeHandler();

        UUID likeUUID = UUID.randomUUID();
        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Тест сообщение";
        String afterContent = SEND_LIKE_POST.concat(content.concat("\""));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(likeUUID);
        notificationDTO.setReceiverId(receiverRandomUUID);
        notificationDTO.setNotificationType(Type.LIKE);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(receiverRandomUUID);

        Like like = new Like();
        like.setAuthorId(randomUUID);
        like.setType(LikeType.POST);
        like.setItemId(likeUUID);

        PostDto postDto = new PostDto();
        postDto.setAuthorId(receiverRandomUUID);
        postDto.setPostText(content);

        when(likeService.getLike(notificationDTO.getAuthorId())).thenReturn(like);

        when(postService.get(like.getItemId())).thenReturn(postDto);

//        when(settingsRepository.findByAccountId(receiverRandomUUID)).thenReturn(settings);
        doReturn(settings).when(settingsRepository).findByAccountId(receiverRandomUUID);

        List<EventNotification> listEventNotifications = likeHandler.getEventNotificationList(notificationDTO);

        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.LIKE);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("content", afterContent);
        }

        assertThat(listEventNotifications.size()).isEqualTo(1);

    }

    /*
    @Test
    @DisplayName("Return List Event Notification with comment")
    void getEventNotificationListComment() {

        likeHandler = getNewLikeHandler();

        UUID likeUUID = UUID.randomUUID();
        UUID randomUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Тест сообщение";
        String afterContent = SEND_LIKE_COMMENT.concat(content.concat("\""));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(likeUUID);
        notificationDTO.setReceiverId(receiverRandomUUID);
        notificationDTO.setNotificationType(Type.LIKE);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(receiverRandomUUID);

        Like like = new Like();
        like.setAuthorId(randomUUID);
        like.setType(LikeType.COMMENT);
        like.setItemId(likeUUID);

        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorId(randomUUID);
        commentDto.setCommentText(content);

        when(likeService.getLike(notificationDTO.getAuthorId())).thenReturn(like);

        when(commentService.get(like.getItemId())).thenReturn(commentDto);

//        when(settingsRepository.findByAccountId(receiverRandomUUID)).thenReturn(settings);
//        when(settingsRepository.findByAccountId(receiverRandomUUID)).then(doReturn(settings));
//        doReturn(settings).when(settingsRepository).findByAccountId(receiverRandomUUID);
//        given().will();
        given(settingsRepository.findByAccountId(receiverRandomUUID)).willReturn(settings);

        List<EventNotification> listEventNotifications = likeHandler.getEventNotificationList(notificationDTO);

        for (EventNotification eventNotification : listEventNotifications) {
            assertThat(eventNotification).hasFieldOrPropertyWithValue("authorId", randomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("receiverId", receiverRandomUUID);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("notificationType", Type.LIKE);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("status", Status.SEND);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("sentTime", zonedDateTime);
            assertThat(eventNotification).hasFieldOrPropertyWithValue("content", afterContent);
        }

        assertThat(listEventNotifications.size()).isEqualTo(1);

    }
    */

    @Test
    @DisplayName("Type isEquals")
    void getType() {
        Assertions.assertEquals(likeHandler.myType(), Type.LIKE);
    }


    private LikeHandler getNewLikeHandler() {
        LikeHandler likeHandler = new LikeHandler(
                likeService,
                commentService,
                postService,
                new NotificationSettingsService(settingsRepository),
                Mappers.getMapper(NotificationsMapper.class));
        return likeHandler;
    }
}
