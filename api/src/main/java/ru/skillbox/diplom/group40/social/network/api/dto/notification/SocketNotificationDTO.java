package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class SocketNotificationDTO<T> {

    @Schema(description = "Тип полученного объекта data (MESSAGE/NOTIFICATION)")
    private String type;
    @Schema(description = "id пользователя, для которого отправляется уведомление")
    private UUID recipientId;
    @Schema(description = "Объект уведомления/сообщения")
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private T data;

}
