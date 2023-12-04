package ru.skillbox.diplom.group40.social.network.api.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.io.Serializable;
import java.util.UUID;

@Data
public class BaseDto implements Serializable {

    @Schema(description = "id записи")
    private UUID id;

    @Schema(description = "Пометка удаления" , defaultValue = "false")
    private Boolean isDeleted;
}
