package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class EventNotificationDTO extends BaseDto {

    UUID authorId;

    UUID receiverId;

    Type notificationType;

    String content;

    Status status;

    ZonedDateTime sentTime;

}
