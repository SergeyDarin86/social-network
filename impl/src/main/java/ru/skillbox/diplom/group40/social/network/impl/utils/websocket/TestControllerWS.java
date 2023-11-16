package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.ReadStatus;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/websocket/add")
public class TestControllerWS {
    ObjectMapper mapper = new ObjectMapper();

    private final NotificationsMapper notificationsMapper;
    private final NotificationService notificationService;
    private final KafkaService kafkaService;

    @PostMapping
    public void publishedTest(@RequestBody SocketNotificationDTO socketNotificationDTO) throws Exception {

        if(socketNotificationDTO.getType().equals("NOTIFICATION")) {
            log.info("\nПолучен POST запрос с type=NOTIFICATION: {}", socketNotificationDTO);
            notificationService.addNotification(notificationsMapper.getEventNotificationDTO(socketNotificationDTO));
            kafkaService.sendSocketNotificationDTO(socketNotificationDTO);
        }

        if(socketNotificationDTO.getType().equals("MESSAGE")) {
            log.info("\nПолучен POST запрос с type=MESSAGE: {}", socketNotificationDTO);
        }

    }

    @GetMapping
    public void testDtoMessage() throws Exception {
        kafkaService.sendSocketNotificationDTO(getWebsocketDtoMessage());
    }

    public String getJSONEvNotDTO() {

        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(Type.LIKE.toString());
        evNotificationDTO.setContent("test");
        evNotificationDTO.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));
        evNotificationDTO.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));

        String jsonDTOString = null;
        try {
            jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(jsonDTOString);
        return jsonDTOString;
    }

    public SocketNotificationDTO getWebsocketDtoMessage() {

        MessageDto messageDto = new MessageDto();
        messageDto.setTime(ZonedDateTime.now());
        messageDto.setConversationPartner1(UUID.fromString("65918f1f-3e1e-4833-8d85-4b83389d6a10"));
        messageDto.setConversationPartner2(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));
        messageDto.setMessageText("test DTO with Message");
        messageDto.setReadStatus(ReadStatus.SENT);
        messageDto.setDialogId(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));

        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(messageDto);
        socketNotificationDTO.setType("MESSAGE");
        socketNotificationDTO.setRecipientId(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));

        log.info("\nСформирована Notification DTO с с type=Message: {}", socketNotificationDTO);
        return socketNotificationDTO;
    }

}
