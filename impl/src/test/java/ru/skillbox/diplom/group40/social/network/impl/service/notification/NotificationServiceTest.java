package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.components.*;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {
                                KafkaService.class, EventNotificationRepository.class,
                                CommentCommentHandler.class, EmailHandler.class, FriendApproveHandler.class,
                                FriendBirthdayHandler.class, FriendBlockedHandler.class, FriendRequestHandler.class,
                                FriendSubscribeHandler.class, FriendUnblockedHandler.class, LikeHandler.class,
                                MessageHandler.class, PostCommentHandler.class, PostHandler.class,
                                UserBirthdayHandler.class,
                                NotificationSettingsService.class, CommentService.class
                                })
//@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {
    @Mock
    CommentService commentService;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    NotificationSettingsService notificationSettingsService/* = new NotificationSettingsService(settingsRepository)*/;
    @Mock
    private NotificationHandler notificationHandler;
    @Mock
    private List<NotificationHandler> notificationHandlerList;
    @Mock
    private CommentCommentHandler commentCommentHandler;
    @InjectMocks
    private EmailHandler emailHandler;
    @Mock
    private FriendApproveHandler friendApproveHandler;
    @Mock
    private FriendBirthdayHandler friendBirthdayHandler;
    @Mock
    private FriendBlockedHandler friendBlockedHandler;
    @Mock
    private FriendRequestHandler friendRequestHandler;
    @Mock
    private FriendSubscribeHandler friendSubscribeHandler;
    @Mock
    private FriendUnblockedHandler friendUnblockedHandler;
    @Mock
    private LikeHandler likeHandler;
    @Mock
    private MessageHandler messageHandler;
    @Mock
    private PostCommentHandler postCommentHandler;
    @Mock
    private PostHandler postHandler;
    @Mock
    private UserBirthdayHandler userBirthdayHandler;
    @Mock
    private EventNotificationRepository eventNotificationRepository;
    @Mock
    private KafkaService kafkaService;
    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);
    private final NotificationMapper notificationMapper = Mappers.getMapper(NotificationMapper.class);

    /*
    private NotificationService notificationService = new NotificationService(
//            notificationHandlerList,
            Arrays.stream(new NotificationHandler[]{ commentCommentHandler, emailHandler, friendApproveHandler}).toList(),
            eventNotificationRepository,
            kafkaService,
            notificationsMapper,
            notificationMapper
    );
    */

    @Test
    @DisplayName("Test")
    void getTest() {
        NotificationService notificationService = getNewNotificationService();
    }

    @Test
    @DisplayName("Create email message")
    void createEmailMessage() {
        NotificationService notificationService = getNewNotificationService();

        UUID randomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Test content";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setNotificationType(Type.SEND_EMAIL_MESSAGE);
        notificationDTO.setSentTime(zonedDateTime);


        /** NULL получаю вместо замоканного репозитория */
        /*
        Settings settings = new Settings();
        settings.setAccountId(randomUUID);
        when(settingsRepository.findByAccountId(randomUUID)).thenReturn(settings);
        */

        when(notificationSettingsService.isNotificationTypeEnables(notificationDTO)).thenReturn(true);

        notificationService.create(notificationDTO);

    }

    private NotificationService getNewNotificationService(){
        NotificationService notificationService = new NotificationService(
                Arrays.stream(new NotificationHandler[]{
                        new CommentCommentHandler(commentService, notificationSettingsService, notificationsMapper),
                        new EmailHandler(notificationSettingsService, notificationsMapper)
                }).toList(),
                eventNotificationRepository,
                kafkaService,
                notificationsMapper,
                notificationMapper
        );
        return notificationService;
    }

    @Test
    @DisplayName("Create email message with NotificationSettingsService")
    void createEmailMessageWithSettings() {
        NotificationService notificationService = getNewNotificationServiceWithSettings();

        UUID randomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(randomUUID);
        notificationDTO.setNotificationType(Type.SEND_EMAIL_MESSAGE);
        notificationDTO.setSentTime(zonedDateTime);

        Settings settings = new Settings();
        settings.setAccountId(randomUUID);
        when(settingsRepository.findByAccountId(randomUUID)).thenReturn(settings);

        notificationService.create(notificationDTO);

    }

    private NotificationService getNewNotificationServiceWithSettings(){
        NotificationService notificationService = new NotificationService(
                Arrays.stream(new NotificationHandler[]{
                        new CommentCommentHandler(commentService, new NotificationSettingsService(settingsRepository),
                                notificationsMapper),
                        new EmailHandler(new NotificationSettingsService(settingsRepository), notificationsMapper)
                }).toList(),
                eventNotificationRepository,
                kafkaService,
                notificationsMapper,
                notificationMapper
        );
        return notificationService;
    }
}
