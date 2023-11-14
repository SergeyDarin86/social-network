package ru.skillbox.diplom.group40.social.network.impl.mapper.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Mapper(componentModel = "spring")
public abstract class NotificationsMapper {

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    protected NotificationsMapper() {
    }

    public List<UUID> getListUUID(List<String> stringListUUIDs) {
        log.info("NotificationsMapper:getListUUI() startMethod - получен List<String>: {}", stringListUUIDs);
        List<UUID> listUUIDs = new ArrayList<>();
        for(String stringId: stringListUUIDs) {
            listUUIDs.add(UUID.fromString(stringId));
        }
        log.info("NotificationsMapper:getListUUI() endMethod - итоговый List<UUID>: {}", listUUIDs);
        return listUUIDs;
    }

    public NotificationDTO postToNotificationDTO(PostDto postDto) {
        log.info("NotificationsMapper:postToNotificationDTO() начало метода - передан Post: {}", postDto);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationType(Type.POST);
        notificationDTO.setContent(postDto.getPostText());
        notificationDTO.setAuthorId(postDto.getAuthorId());
        notificationDTO.setSentTime(ZonedDateTime.now());                                                                               /** т.к. в postDto время присваивается после передачи нотификации, при возврате*/
        log.info("NotificationsMapper:postToNotificationDTO() конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationDTO likeToNotificationDTO(LikeDto likeDto) {
        log.info("NotificationsMapper:likeToNotificationDTO(LikeDto likeDto) начало метода - передан LikeDto: {}", likeDto);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(likeDto.getAuthorId());
        /**Исправить после перключения*/
        ZoneId zoneId = Clock.systemUTC().getZone();
        notificationDTO.setSentTime(likeDto.getTime().atZone(zoneId));
        notificationDTO.setContent("");
        notificationDTO.setNotificationType(Type.LIKE);

        log.info("NotificationsMapper:likeToNotificationDTO(LikeDto likeDto) конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationDTO likeToNotificationDTO(Like like) {
        log.info("NotificationsMapper:likeToNotificationDTO(Like like) начало метода - передан Like: {}", like);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(like.getId());
        /**Исправить после перключения*/
        ZoneId zoneId = Clock.systemUTC().getZone();
        notificationDTO.setSentTime(like.getTime().atZone(zoneId));
        notificationDTO.setContent("");
        notificationDTO.setNotificationType(Type.LIKE);

        log.info("NotificationsMapper:likeToNotificationDTO(Like like) конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationDTO commentToNotificationDTO(CommentDto commentDto) {
        log.info("NotificationsMapper:commentToNotificationDTO(_) начало метода - передан CommentDto: {}", commentDto);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(commentDto.getAuthorId());
        /**Исправить после перключения*/
        ZoneId zoneId = Clock.systemUTC().getZone();
        notificationDTO.setSentTime(commentDto.getTime().atZone(zoneId));
        notificationDTO.setContent(commentDto.getCommentText());

        if(commentDto.getCommentType().equals(CommentType.POST)){notificationDTO.setNotificationType(Type.POST_COMMENT);}
        else {notificationDTO.setNotificationType(Type.COMMENT_COMMENT);}

        log.info("NotificationsMapper:commentToNotificationDTO() конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationDTO commentCommentToNotificationDTO(CommentDto commentDto) {
        log.info("NotificationsMapper:commentToNotificationDTO(_) начало метода - передан CommentDto: {}", commentDto);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(commentDto.getAuthorId());
        /**Исправить после перключения*/
        ZoneId zoneId = Clock.systemUTC().getZone();
        notificationDTO.setSentTime(commentDto.getTime().atZone(zoneId));
        notificationDTO.setContent(commentDto.getCommentText());
        notificationDTO.setNotificationType(Type.COMMENT_COMMENT);

        log.info("NotificationsMapper:commentToNotificationDTO() конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationDTO getNotificationDTO(Message message) {
        log.info("NotificationsMapper:getNotificationDTO(Message message) начало метода - передан friend: {}", message);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(message.getId());
        /**Исправить после перключения*/
        ZoneId zoneId = Clock.systemUTC().getZone();
        notificationDTO.setSentTime(message.getTime().atZone(zoneId));
        notificationDTO.setContent(message.getMessageText());
        notificationDTO.setNotificationType(Type.MESSAGE);

        log.info("NotificationsMapper:getNotificationDTO(Friend friend) конец метода - получен NotificationDTO: {}",
                notificationDTO);
        return notificationDTO;
    };

    public NotificationsDTO getEmptyAllNotificationsDTO(UUID id) {
        NotificationsDTO notificationsDTO = new NotificationsDTO();
        notificationsDTO.setTotalPages(1);
        notificationsDTO.setTotalElements(2);
        notificationsDTO.setNumber(3);
        notificationsDTO.setSize(4);

        ArrayList<ContentDTO> Contents = new ArrayList<>();
        notificationsDTO.setContent(Contents);

        SortDTO testSort = new SortDTO();
        testSort.setEmpty(true);
        testSort.setSorted(true);
        testSort.setUnsorted(true);
        notificationsDTO.setSort(testSort);

        notificationsDTO.setFirst(true);
        notificationsDTO.setLast(true);
        notificationsDTO.setNumberOfElements(5);
        notificationsDTO.setEmpty(true);

        return notificationsDTO;
    }

    public NotificationDTO createNotificationDTO(EventNotification eventNotification) {
        NotificationDTO notification = new NotificationDTO();
        notification.setIsDeleted(false);
        notification.setAuthorId(eventNotification.getAuthorId());
        notification.setContent(eventNotification.getContent());
        notification.setNotificationType(eventNotification.getNotificationType());
        notification.setSentTime(eventNotification.getSentTime());
        return notification;
    }


    public  EventNotification createEventNotification(NotificationDTO notificationDTO, UUID accountId) {
        EventNotification eventNotification = new EventNotification();
        eventNotification.setNotificationType(notificationDTO.getNotificationType());
        eventNotification.setContent(notificationDTO.getContent());
        eventNotification.setAuthorId(notificationDTO.getAuthorId());
        eventNotification.setReceiverId(accountId);
        eventNotification.setIsDeleted(false);
        eventNotification.setStatus(Status.SEND);
        eventNotification.setSentTime(notificationDTO.getSentTime());
        return eventNotification;
    }

    public  EventNotification createEventNotification(NotificationDTO notificationDTO) {
        return createEventNotification(notificationDTO, notificationDTO.getReceiverId());
    }

    public ContentDTO eventNotificationToContentDTO(EventNotification eventNotification) {
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTimeStamp(eventNotification.getSentTime());
        NotificationDTO notification = createNotificationDTO(eventNotification);
        contentDTO.setData(notification);
        return contentDTO;
    }

    public NotificationDTO createTestNotificationDTO(UUID id) {
        NotificationDTO notification = new NotificationDTO();
        notification.setId(id);
        notification.setIsDeleted(false);
        notification.setAuthorId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        notification.setContent("TestContent");
        notification.setNotificationType(Type.POST);
        notification.setSentTime(ZonedDateTime.now());
        return notification;
    }

    public CountDTO getCountDTO(int count) {
        PartCountDTO partCountDTO = new PartCountDTO();
        partCountDTO.setCount(count);
        CountDTO countDTO = new CountDTO();
        countDTO.setTimeStamp(ZonedDateTime.now());
        countDTO.setData(partCountDTO);
        return countDTO;
    }

    public SocketNotificationDTO getSocketNotificationDTO(NotificationDTO notificationDTO, UUID accountId) {

        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(notificationDTO.getNotificationType().toString());
        evNotificationDTO.setContent(notificationDTO.getContent());
        evNotificationDTO.setAuthorId(notificationDTO.getAuthorId());
        evNotificationDTO.setReceiverId(accountId);

        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(accountId);

        return socketNotificationDTO;
    }

    public List<SocketNotificationDTO> getListSocketNotificationDTO(List<EventNotification> listEventNotifications) {
        List<SocketNotificationDTO> listSocketNotificationDTO = new ArrayList<>();

        for(EventNotification eventNotification : listEventNotifications) {
            listSocketNotificationDTO.add(getSocketNotificationDTO(eventNotification));
        }

        return listSocketNotificationDTO;
    }

    public SocketNotificationDTO getSocketNotificationDTO(EventNotification eventNotification) {

        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(eventNotification.getNotificationType().toString());
        evNotificationDTO.setContent(eventNotification.getContent());
        evNotificationDTO.setAuthorId(eventNotification.getAuthorId());
        evNotificationDTO.setReceiverId(eventNotification.getReceiverId());

        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(eventNotification.getReceiverId());

        return socketNotificationDTO;
    }

    public String getJSON(SocketNotificationDTO socketNotificationDTO) {
        log.info("NotificationsMapper: getJSON startMethod, SocketNotificationDTO: {}", socketNotificationDTO);

        String jsonDTOString = null;
        try {
            jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
            log.info("NotificationsMapper: getJSON finishMethod, jsonDTOString: {}", jsonDTOString);
        return jsonDTOString;
    }

    public SocketNotificationDTO getSocketNotificationDTO(String json) {

        try {
            SocketNotificationDTO socketNotificationDTO = mapper.readValue(json, SocketNotificationDTO.class);
            return socketNotificationDTO;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSocketNotificationJSON(NotificationDTO notificationDTO, UUID accountId) {
        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(notificationDTO.getNotificationType().toString());
        evNotificationDTO.setContent(notificationDTO.getContent());
        evNotificationDTO.setAuthorId(notificationDTO.getAuthorId());
        evNotificationDTO.setReceiverId(accountId);
        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(accountId);

        String jsonDTOString = null;
        try {
            jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("NotificationsMapper: getSocketNotificationJSON() String jsonDTOString: {}", jsonDTOString);
        return jsonDTOString;
    }

    public EventNotificationDTO getEventNotificationDTO(SocketNotificationDTO socketNotificationDTO) {

        EvNotificationDTO data = (EvNotificationDTO) socketNotificationDTO.getData();

        EventNotificationDTO eventNotificationDTO = new EventNotificationDTO();
        eventNotificationDTO.setAuthorId(data.getAuthorId());
        eventNotificationDTO.setReceiverId(data.getReceiverId());
        eventNotificationDTO.setNotificationType(Type.valueOf(data.getNotificationType()));
        eventNotificationDTO.setContent(data.getContent());
        eventNotificationDTO.setStatus(Status.SEND);

        return  eventNotificationDTO;
    }

    public AccountOnlineDto getAccountOnlineDto(UUID uuid, Boolean isOnline) {
        AccountOnlineDto accountOnlineDto = new AccountOnlineDto();
        accountOnlineDto.setIsOnline(isOnline);
        /**Исправить после перключения*/
        accountOnlineDto.setLastOnlineTime(ZonedDateTime.now().toLocalDateTime());
        accountOnlineDto.setId(uuid);
        log.info("NotificationsMapper: getAccountOnlineDto(_) - сформирована AccountOnlineDto : {}", accountOnlineDto);
        return  accountOnlineDto;
    }

}
