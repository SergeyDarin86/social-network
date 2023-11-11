package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EvNotificationDTO {

    private UUID authorId;
    private UUID receiverId;
    private String notificationType;
    private String content;

}

