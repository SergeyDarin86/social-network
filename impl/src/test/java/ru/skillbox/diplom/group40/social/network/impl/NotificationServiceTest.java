package ru.skillbox.diplom.group40.social.network.impl.unit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.CommentRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.utils.technikalUser.TechnicalUserConfig;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class NotificationServiceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EventNotificationRepository eventNotificationRepository;
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    JwtEncoder jwtEncoder;
    @Autowired
    TechnicalUserConfig technicalUser;
    @Autowired
    JwtEncoder accessTokenEncoder;
    @Autowired
    NotificationsMapper notificationsMapper;
    @Autowired
    CommentMapperImpl commentMapper;
//    private static FactoryTest factoryTest;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withInitScript("ru/skillbox/diplom/group40/social/network/impl/schema.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
//        factoryTest = new FactoryTest();
        postgres.start();
    }

    @Test
    @Transactional
    @DisplayName("Test create COMMENT_COMMENT")
    public void create() {
        technicalUser.executeByTechnicalUser(() -> 2);

        UUID authorUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime zonedDateTime2 = ZonedDateTime.now().minusHours(1);
        String content = "Тест сообщение";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(authorUUID);
        notificationDTO.setContent(content);
        notificationDTO.setNotificationType(Type.COMMENT_COMMENT);
        notificationDTO.setSentTime(zonedDateTime);

        Settings receiverSettings = new Settings();
        receiverSettings.setAccountId(receiverRandomUUID);
        Settings afterSaveReceiverSettings = settingsRepository.save(receiverSettings);


        Comment commentParent = new Comment();
        commentParent.setAuthorId(receiverRandomUUID);
        commentParent.setTime(zonedDateTime2.toLocalDateTime());
        Comment afterSaveCommentParent = commentRepository.save(commentParent);

        Comment comment = new Comment();
        comment.setParentId(commentParent.getId());
        comment.setAuthorId(authorUUID);
        comment.setTime(zonedDateTime.toLocalDateTime());
        Comment afterSaveComment = commentRepository.save(comment);

        notificationService.create(notificationDTO);

        assertDoesNotThrow(() -> notificationService.create(notificationDTO));
//        assertThat(person.getAge())
//        assertThrows(Exception.class, () -> notificationService.create(notificationDTO));

    }

    @Test
    @Transactional
    @DisplayName("Test create COMMENT_COMMENT with Exception not found correct UUID ParentId in comment ")
    public void createException() {
        technicalUser.executeByTechnicalUser(() -> 2);

        UUID authorUUID = UUID.randomUUID();
        UUID receiverRandomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime zonedDateTime2 = ZonedDateTime.now().minusHours(1);
        String content = "Тест сообщение";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(authorUUID);
        notificationDTO.setContent(content);
        notificationDTO.setNotificationType(Type.COMMENT_COMMENT);
        notificationDTO.setSentTime(zonedDateTime);

        Settings receiverSettings = new Settings();
        receiverSettings.setAccountId(receiverRandomUUID);
        Settings afterSaveReceiverSettings = settingsRepository.save(receiverSettings);


        Comment commentParent = new Comment();
        commentParent.setAuthorId(receiverRandomUUID);
        commentParent.setTime(zonedDateTime2.toLocalDateTime());
        Comment afterSaveCommentParent = commentRepository.save(commentParent);

        Comment comment = new Comment();
        comment.setParentId(UUID.randomUUID());
        comment.setAuthorId(authorUUID);
        comment.setTime(zonedDateTime.toLocalDateTime());
        Comment afterSaveComment = commentRepository.save(comment);

        assertThrows(Exception.class, () -> notificationService.create(notificationDTO));

    }

    @Test
    @Transactional
    void test() {
        technicalUser.executeByTechnicalUser(() -> 2);
        Assertions.assertEquals(1, 1);
    }


}
