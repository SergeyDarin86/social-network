package ru.skillbox.diplom.group40.social.network.api.dto.geo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.UUID;

@Data
public class CityDto extends BaseDto{
    @Schema(description = "Название страны")
    private String title;
    @Schema(description = "id страны")
    private UUID countryId;
}
