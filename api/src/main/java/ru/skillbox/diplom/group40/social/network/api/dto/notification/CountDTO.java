package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CountDTO {

    @Schema(description = "Время обработки запроса на количество непрочитонных пользователем уведомлений")
    private ZonedDateTime timeStamp;
    @Schema(description = "Объект, содержащий количество непрочитонных пользователем уведомлений")
    private PartCountDTO data;

}