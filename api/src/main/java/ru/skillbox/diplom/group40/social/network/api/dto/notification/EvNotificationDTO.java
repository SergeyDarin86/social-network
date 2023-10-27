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

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
//    private Date sentTime = new Date();      //      test
//private LocalDateTime sentTime = LocalDateTime.now();      //      test