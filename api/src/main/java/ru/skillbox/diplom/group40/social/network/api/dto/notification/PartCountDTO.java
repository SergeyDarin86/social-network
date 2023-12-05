package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PartCountDTO {
    @Schema(description = "Количество непрочитонных пользователем уведомлений")
    private long count;
}
