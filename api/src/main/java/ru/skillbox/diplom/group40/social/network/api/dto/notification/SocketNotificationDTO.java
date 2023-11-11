package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;

import java.util.UUID;

@Data
public class SocketNotificationDTO<T> {

    private String type;
    private UUID recipientId;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private T data;

}
