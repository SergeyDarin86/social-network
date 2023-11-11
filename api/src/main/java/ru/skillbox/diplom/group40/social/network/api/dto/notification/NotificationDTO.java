package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO extends BaseDto {

    UUID authorId;
    UUID receiverId;
    String content;
    Type notificationType;
    ZonedDateTime sentTime;

}
