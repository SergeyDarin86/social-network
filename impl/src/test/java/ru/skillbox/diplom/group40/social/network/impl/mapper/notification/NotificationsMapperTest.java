package ru.skillbox.diplom.group40.social.network.impl.mapper.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationsMapperTest {

    private NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);

    /*
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1")
            .withReuse(true)
            .withDatabaseName("skillbox");
    */

    @Test
    @DisplayName("Convert List String to List UUID")
    void getListUUID() {
        List<String> stringListUUIDs = new ArrayList<>();
        List<UUID> listUUID = new ArrayList<>();

        // TODO: Вынести в общий класс с тестовыми данными
        for (int i = 1; i < 5; i++) {
            UUID random = UUID.randomUUID();
            listUUID.add(random);
            stringListUUIDs.add(random.toString());
        }

        List<UUID> afterListUUID = notificationsMapper.getListUUID(stringListUUIDs);

        /** Поломка для проверки:*/
//        listUUID = new ArrayList<>();

        Assertions.assertEquals(listUUID, afterListUUID, "Lists");

    }

    @Test
    @DisplayName("Convert PostDTO to NotificationDTO")
    void postToNotificationDTO() {

        // TODO: Вынести в общий класс с тестовыми данными
        UUID randomUUID = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String content = "Test content";

        PostDto originalPostDto = new PostDto();
        originalPostDto.setAuthorId(randomUUID);
        originalPostDto.setPostText(content);

        NotificationDTO originalNotificationDTO = new NotificationDTO();
        originalNotificationDTO.setNotificationType(Type.POST);
        originalNotificationDTO.setContent(content);
        originalNotificationDTO.setAuthorId(randomUUID);
        originalNotificationDTO.setSentTime(zonedDateTime);

        NotificationDTO notificationDTO = notificationsMapper.postToNotificationDTO(originalPostDto);
        notificationDTO.setSentTime(zonedDateTime);

        /** Поломка для проверки:*/
//        randomUUID = UUID.randomUUID();
//        notificationDTO.setContent("error");
//        notificationDTO.setNotificationType(Type.LIKE);
//        notificationDTO.setSentTime(ZonedDateTime.now());

        assertThat(notificationDTO).hasFieldOrPropertyWithValue("authorId", randomUUID);
        assertThat(notificationDTO).hasFieldOrPropertyWithValue("content", "Test content");
        assertThat(notificationDTO).hasFieldOrPropertyWithValue("notificationType", Type.POST);

        Assertions.assertEquals(originalNotificationDTO, notificationDTO);
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*

@DisplayName("RandomSort")
@ParameterizedTest
@ValueSource(ints = {2,3,4,5})

@DisplayName("IsSorted Array")
@ParameterizedTest
@MethodSource("arrays")
*/