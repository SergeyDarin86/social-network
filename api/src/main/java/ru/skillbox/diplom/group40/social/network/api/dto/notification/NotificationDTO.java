package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDTO extends BaseDto {

    UUID authorId;
    String content;
    Type notificationType;
    LocalDateTime sentTime;

}