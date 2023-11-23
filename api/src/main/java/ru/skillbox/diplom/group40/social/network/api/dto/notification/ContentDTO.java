package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ContentDTO {

    @Schema(description = "Время события (поста, запроса на дружбу и т.д.), создавшего уведомление")
    private ZonedDateTime timeStamp;
    @Schema(description = "Объект нотификации, с заполненными необходимыми полями")
    private NotificationDTO data;

}
