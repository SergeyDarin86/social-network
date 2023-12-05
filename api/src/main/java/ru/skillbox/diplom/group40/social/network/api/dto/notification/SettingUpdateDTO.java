package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettingUpdateDTO {

    @Schema(description = "Отметка состояния, для приложенного типа нотификации")
    private boolean enable;
    @Schema(description = "Тип нотификации")
    private Type notificationType;

}
